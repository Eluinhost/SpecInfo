package gg.uhc.specinfo.listeners.veins;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;

public class VeinCache {

    protected final Set<VeinInformation> veins = Sets.newIdentityHashSet();
    protected final Map<Block, VeinInformation> blocks = Maps.newHashMap();
    protected final Set<Block> trackedBlocks = blocks.keySet();
    protected final Map<VeinInformation, TimedRemover> removalTimers = Maps.newIdentityHashMap();

    protected final VeinTraverser veinTraverser;
    protected final Plugin plugin;
    protected final int keepAlive;

    public VeinCache(Plugin plugin, VeinTraverser veinTraverser, int keepAlive) {
        this.plugin = plugin;
        this.veinTraverser = veinTraverser;
        this.keepAlive = keepAlive;
    }

    /**
     * Retreives the existing vein at the given block or creates a new one if it doesn't exist.
     * If the vein already exists then it's timeout before removal is extended via refreshVeinTimer.
     *
     * @param block the block to lookup
     * @return a vein at the given block
     */
    public VeinInformation getOrCreateVein(Block block) {
        VeinInformation vein = blocks.get(block);

        // return if we already have one
        if (vein != null) {
            refreshVeinTimer(vein);
            return vein;
        }

        // get a vein at the location
        vein = veinTraverser.getMatchingVeinAtBlock(block, trackedBlocks);

        // store the information
        addVein(vein);

        return vein;
    }

    /**
     * Adds the given vein and starts a timer for it's removal. If the vein already exists this method does nothing.
     *
     * @param vein the vein to add
     */
    public void addVein(VeinInformation vein) {
        if (veins.contains(vein)) return;

        // add each location to the map
        for (Block b : vein.getBlocks()) {
            blocks.put(b, vein);
        }

        // add to set of veins
        veins.add(vein);

        // time the removal of the vein
        refreshVeinTimer(vein);
    }

    /**
     * Removes the given vein from the cache
     *
     * @param vein the vein to remove
     */
    public void removeVein(VeinInformation vein) {
        // cancel and remove it's timer
        TimedRemover remover = removalTimers.remove(vein);
        if (remover != null) remover.cancel();

        // remove the vein from the list
        if (veins.remove(vein)) {
            // only remove the blocks if
            // the vein still existed
            trackedBlocks.removeAll(vein.getBlocks());
        }
    }

    /**
     * Restarts the timer for the given vein.
     *
     * @param vein the vein to refresh
     * @return true if timer was refreshed, false if the vein was expired/isnt being tracked (not refreshed)
     */
    public boolean refreshVeinTimer(VeinInformation vein) {
        if (!veins.contains(vein)) return false;

        TimedRemover newTimer = new TimedRemover(vein);
        TimedRemover existing = removalTimers.put(vein, newTimer);

        // cancel existing if there was one
        if (existing != null) {
            existing.cancel();
        }

        // schedule new one
        newTimer.runTaskLater(plugin, keepAlive);

        return true;
    }

    class TimedRemover extends BukkitRunnable {

        protected final VeinInformation vein;

        TimedRemover(VeinInformation vein) {
            this.vein = vein;
        }

        @Override
        public void run() {
            removeVein(vein);
        }
    }
}
