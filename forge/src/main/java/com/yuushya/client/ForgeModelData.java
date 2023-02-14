package com.yuushya.client;

import com.yuushya.mappings.ForgeModelDataMapper;
import com.yuushya.showblock.ShowBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ForgeModelData extends ForgeModelDataMapper {
    public ForgeModelData(BlockEntity blockEntity) {
        super(blockEntity);
        if (blockEntity instanceof ShowBlock.ShowBlockEntity) {
            check = true;
        }
    }
}