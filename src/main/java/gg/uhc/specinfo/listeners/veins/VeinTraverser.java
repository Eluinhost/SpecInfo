package gg.uhc.specinfo.listeners.veins;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.LinkedList;
import java.util.Set;

public class VeinTraverser {

    protected final int maxSize;

    public VeinTraverser(int maxSize) {
        this.maxSize = maxSize;
    }

    public VeinInformation getMatchingVeinAtBlock(Block block, Set<Block> exclude) {
        Preconditions.checkNotNull(block);

        Material type = block.getType();

        Set<Block> matching = Sets.newHashSet(block);

        // FIFO list of blocks awaiting checks
        LinkedList<Block> waitingForChecks = Lists.newLinkedList();
        waitingForChecks.add(block);

        int x,y,z;
        Block current, check;

        outer:
        while (waitingForChecks.size() > 0) {
            // remove the first item in the list
            current = waitingForChecks.poll();

            // for every surrounding block
            for (x = -1; x <= 1; x++) {
                for (y = -1; y <= 1; y++) {
                    for (z = -1; z<= 1; z++) {

                        // get block at the location
                        check = current.getRelative(x, y, z);

                        // skip outside world and wrong types
                        if (check == null || check.getType() != type) continue;

                        // skip excluded locations
                        if (exclude.contains(check)) continue;

                        // add to matching set
                        if (matching.add(check)) {
                            // quit out of loop if we hit max size
                            if (matching.size() >= maxSize) {
                                break outer;
                            }

                            // only add to waiting list if it wasnt
                            // already in the matching set
                            waitingForChecks.add(check);
                        }
                    }
                }
            }
        }

        return new VeinInformation(type, matching);
    }
}
