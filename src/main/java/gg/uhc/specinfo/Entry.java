package gg.uhc.specinfo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import gg.uhc.specinfo.command.SpecInfoCommand;
import gg.uhc.specinfo.command.SpectatorTPCommand;
import gg.uhc.specinfo.listeners.CraftItemListener;
import gg.uhc.specinfo.listeners.DamageTakenListener;
import gg.uhc.specinfo.listeners.ItemConsumeListener;
import gg.uhc.specinfo.listeners.PotionListener;
import gg.uhc.specinfo.listeners.veins.VeinBreakListener;
import gg.uhc.specinfo.listeners.veins.VeinCache;
import gg.uhc.specinfo.listeners.veins.VeinTraverser;
import gg.uhc.specinfo.log.ConsoleMessageLogger;
import gg.uhc.specinfo.log.MessageLogger;
import gg.uhc.specinfo.log.MultiMessageLogger;
import gg.uhc.specinfo.log.SpectatorsMessageLogger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class Entry extends JavaPlugin {

    @Override
    public void onEnable() {
        ChatSender sender;
        try {
            sender = new ChatSender(this);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            setEnabled(false);
            getLogger().severe("This version of Spigot is not supported by this plugin");
            return;
        }

        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        Set<Material> crafting;
        Set<Material> veins;
        Set<Material> eating;
        int maxVeinSize;
        int storeVeinTicks;
        try {
            crafting = readMaterialList(config, "crafting items");
            veins = readMaterialList(config, "digging blocks");
            eating = readMaterialList(config, "eating items");
            maxVeinSize = config.getInt("max vein size");
            storeVeinTicks = config.getInt("store vein ticks");

            Preconditions.checkArgument(maxVeinSize > 0, "Max vein size must be greater than 0");
            Preconditions.checkArgument(storeVeinTicks > 0, "Store vein ticks must be greater than 0");
        } catch (InvalidConfigurationException | IllegalArgumentException ex) {
            ex.printStackTrace();
            setEnabled(false);
            getLogger().severe("Invalid configuration, could not start up plugin");
            return;
        }

        MessageLogger specsAndConsole = new MultiMessageLogger(new ConsoleMessageLogger(), new SpectatorsMessageLogger(sender));

       VeinCache veinCache = new VeinCache(this, new VeinTraverser(maxVeinSize), storeVeinTicks);

        VeinBreakListener veinBreakListener = new VeinBreakListener(veinCache, specsAndConsole, veins);
        List<Listener> listeners = Lists.newArrayList(
                new CraftItemListener(specsAndConsole, crafting),
                veinBreakListener,
                new ItemConsumeListener(specsAndConsole, eating),
                new PotionListener(specsAndConsole),
                new DamageTakenListener(specsAndConsole)
        );

        // register events
        PluginManager manager = getServer().getPluginManager();
        for (Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }

        // register commands
        getCommand("silenttp").setExecutor(new SpectatorTPCommand());
        getCommand("specinfo").setExecutor(new SpecInfoCommand(veinBreakListener));
    }

    protected Set<Material> readMaterialList(ConfigurationSection section, String key) throws InvalidConfigurationException {
        if (!section.isList(key)) throw new InvalidConfigurationException("Expected a list at `" + key + "`");

        List<String> strings = section.getStringList(key);

        Set<Material> materials = Sets.newHashSet();
        for (String string : strings) {
            Material material = Material.getMaterial(string);

            if (null == material) throw new InvalidConfigurationException("Invalid material found in list `" + key + "`: " + string);

            materials.add(material);
        }

        return materials;
    }
}
