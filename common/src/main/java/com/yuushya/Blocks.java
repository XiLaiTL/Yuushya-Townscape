package com.yuushya;

import com.yuushya.showblock.ShowBlock;
import net.minecraft.world.level.block.Block;

public interface Blocks {
    RegistryObject<Block> SHOW_BLOCK = new RegistryObject<>(ShowBlock::new);
}