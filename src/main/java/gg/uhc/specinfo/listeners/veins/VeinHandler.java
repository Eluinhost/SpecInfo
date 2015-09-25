package gg.uhc.specinfo.listeners.veins;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class VeinHandler {

    protected final List<VeinInformation> veins = Lists.newArrayList();
    protected final Map<Block, VeinInformation> blocks = Maps.newHashMap();
    protected final Set<Block> trackedBlocks = blocks.keySet();

    protected final VeinTraverser veinTraverser;
    protected final Plugin plugin;
    protected final int keepAlive;

    public VeinHandler(Plugin plugin, VeinTraverser veinTraverser, int keepAlive) {
        this.plugin = plugin;
        this.veinTraverser = veinTraverser;
        this.keepAlive = keepAlive;
    }

    public VeinInformation getOrCreateVein(Block block) {
        VeinInformation vein = blocks.get(block);

        // return if we already have one
        if (vein != null) {
            return vein;
        }

        // get a vein at the location
        vein = veinTraverser.getMatchingVeinAtBlock(block, trackedBlocks);

        // store the information
        addVein(vein);

        return vein;
    }

    protected void addVein(VeinInformation vein) {
        // add each location to the map
        for (Block b : vein.getBlocks()) {
            blocks.put(b, vein);
        }

        // add to list of veins
        veins.add(vein);

        // time the removal of the vein
        new TimedRemover(vein).runTaskLater(plugin, keepAlive);
    }

    public void removeVein(VeinInformation vein) {
        if (veins.remove(vein)) {
            // only remove the blocks if
            // the vein still exists
            trackedBlocks.removeAll(vein.getBlocks());
        }
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
