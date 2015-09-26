package gg.uhc.specinfo.listeners.veins;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import gg.uhc.specinfo.log.MessageLogger;
import gg.uhc.specinfo.log.extras.LocationTeleportClickable;
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

    protected static final String LOG_FORMAT = "Dug %s (%d/%d %dT)";

    protected final VeinCache cache;
    protected final MessageLogger sendTo;
    protected final Set<Material> check;
    protected final Map<UUID, Multiset<Material>> tracking;

    public VeinBreakListener(VeinCache cache, MessageLogger sendTo, Set<Material> check) {
        this.cache = cache;
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

        VeinInformation vein = cache.getOrCreateVein(block);

        vein.setBlockAsDug(block);

        int total = vein.getBlockCount();
        int dug = vein.getDugCount();

        sendTo.logMessage(player,
                String.format(
                        LOG_FORMAT,
                        type.name(),
                        dug, total, counts.count(type)
                ),
                new LocationTeleportClickable(block.getLocation())
        );

        // remove the vein if it was dug all out
        if (dug == total) {
            cache.removeVein(vein);
        }
    }

    public void clearTracking() {
        tracking.clear();
    }
}
