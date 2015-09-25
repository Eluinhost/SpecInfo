package gg.uhc.specinfo.listeners;

import gg.uhc.specinfo.log.MessageLogger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class CraftItemListener implements Listener {

    protected static final String LOG_FORMAT = "%s crafted a %s";

    protected final MessageLogger sendTo;
    protected Set<Material> logMaterials;

    public CraftItemListener(MessageLogger sender, Set<Material> logMaterials) {
        this.sendTo = sender;
        this.logMaterials = logMaterials;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(CraftItemEvent event) {
        ItemStack crafted = event.getInventory().getResult();

        if (crafted == null) return;

        if (!logMaterials.contains(crafted.getType())) return;

        String itemName = crafted.getType().name();

        ItemMeta meta = crafted.getItemMeta();

        if (meta.hasDisplayName()) {
            itemName += " (" + meta.getDisplayName() + ")";
        }

        Player clicked = (Player) event.getWhoClicked();

        sendTo.logFormattedMessage(clicked, LOG_FORMAT, clicked.getName(), itemName);
    }
}
