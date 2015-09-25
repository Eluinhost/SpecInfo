package gg.uhc.specinfo.listeners.veins;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import gg.uhc.specinfo.log.MessageLogger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class VeinBreakListener implements Listener {

    protected static final String LOG_FORMAT = "%s dug %s (%d/%d %dT) at %d:%d:%d";

    protected final VeinHandler handler;
    protected final MessageLogger sendTo;
    protected final Set<Material> check;
    protected final Map<UUID, Multiset<Material>> tracking;

    public VeinBreakListener(VeinHandler handler, MessageLogger sendTo, Set<Material> check) {
        this.handler = handler;
        this.sendTo = sendTo;
        this.check = check;
        this.tracking = Maps.newHashMap();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(BlockBreakEvent event) {
        Block block = event.getBlock();

        // not a tracked type
        if (!check.contains(block.getType())) return;

        Player player = event.getPlayer();
        Material type = block.getType();

        // increment totals first
        Multiset<Material> counts = tracking.get(player.getUniqueId());

        // create player set if it doesn't exist
        if (counts == null) {
            counts = HashMultiset.create();
            tracking.put(player.getUniqueId(), counts);
        }

        // increment the material type
        counts.add(type);

        VeinInformation vein = handler.getOrCreateVein(block);

        vein.setBlockAsDug(block);

        int total = vein.getBlockCount();
        int dug = vein.getDugCount();

        sendTo.logFormattedMessage(
                player,
                LOG_FORMAT,
                player.getName(),
                type.name(),
                dug, total, counts.count(type),
                block.getX(), block.getY(), block.getZ()
        );

        // remove the vein if it was dug all out
        if (dug == total) {
            handler.removeVein(vein);
        }
    }

    public void clearTracking() {
        tracking.clear();
    }
}
