package gg.uhc.specinfo.listeners;

import gg.uhc.specinfo.log.MessageLogger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class ItemConsumeListener implements Listener {

    protected static final String LOG_FORMAT = "Ate a %s";

    protected final MessageLogger sendTo;
    protected final Set<Material> listen;

    public ItemConsumeListener(MessageLogger sendTo, Set<Material> listen) {
        this.sendTo = sendTo;
        this.listen = listen;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerItemConsumeEvent event) {
        ItemStack ate = event.getItem();

        if (!listen.contains(ate.getType())) return;

        String itemName = ate.getType().name();

        ItemMeta meta = ate.getItemMeta();

        if (meta.hasDisplayName()) {
            itemName += " (" + meta.getDisplayName() + ")";
        }

        Player player = event.getPlayer();

        sendTo.logMessage(player, String.format(LOG_FORMAT, itemName));
    }
}
