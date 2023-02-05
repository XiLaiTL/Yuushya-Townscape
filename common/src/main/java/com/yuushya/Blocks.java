package com.yuushya;

import com.yuushya.blocks.ShowBlock;
import net.minecraft.world.level.block.Block;

public interface Blocks {
    RegistryObject<Block> SHOW_BLOCK = new RegistryObject<>(ShowBlock::new);
}
