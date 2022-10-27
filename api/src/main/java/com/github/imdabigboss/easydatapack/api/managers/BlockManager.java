package com.github.imdabigboss.easydatapack.api.managers;

import com.github.imdabigboss.easydatapack.api.blocks.CustomBlock;
import org.bukkit.block.Block;

import java.util.List;

public interface BlockManager {
    List<CustomBlock> getCustomBlocks();

    CustomBlock blockToCustomBlock(Block block);
}
