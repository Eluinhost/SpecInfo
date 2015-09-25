package gg.uhc.specinfo.listeners.veins;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.Set;

public class VeinInformation {

    protected final Material type;

    protected ImmutableSet<Block> blocks;
    protected Set<Block> dugBlocks;

    public VeinInformation(Material type, Collection<Block> blocks) {
        this.type = type;
        this.blocks = ImmutableSet.copyOf(blocks);
        this.dugBlocks = Sets.newHashSet();
    }

    public Material getType() {
        return type;
    }

    public int getDugCount() {
        return dugBlocks.size();
    }

    public ImmutableSet<Block> getBlocks() {
        return blocks;
    }

    public int getBlockCount() {
        return blocks.size();
    }

    public void setBlockAsDug(Block block) {
        Preconditions.checkArgument(blocks.contains(block));

        dugBlocks.add(block);
    }
}
