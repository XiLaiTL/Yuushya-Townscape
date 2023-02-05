package com.yuushya;

import com.yuushya.blocks.ShowBlock;
import com.yuushya.mappings.RegistryUtilities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockEntityTypes {
    RegistryObject<BlockEntityType<ShowBlock.TileEntityShowBlock>> SHOW_BLOCK_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(ShowBlock.TileEntityShowBlock::new, Blocks.SHOW_BLOCK.get()));
}
