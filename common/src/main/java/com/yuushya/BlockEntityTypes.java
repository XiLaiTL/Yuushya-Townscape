package com.yuushya;

import com.yuushya.mappings.RegistryUtilities;
import com.yuushya.showblock.ShowBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockEntityTypes {
    RegistryObject<BlockEntityType<ShowBlock.ShowBlockEntity>> SHOW_BLOCK_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(ShowBlock.ShowBlockEntity::new, Blocks.SHOW_BLOCK.get()));
}