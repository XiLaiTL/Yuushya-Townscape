package com.yuushya.client;

import com.yuushya.mappings.BakedModelMapper;
import com.yuushya.mappings.ForgeBakedModelMapper;
import com.yuushya.mappings.ForgeModelDataMapper;
import com.yuushya.showblock.ShowBlock;
import com.yuushya.showblock.ShowBlockModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.Collections;
import java.util.List;

public class ShowBlockModelForge extends ShowBlockModel implements BakedModelMapper, ForgeBakedModelMapper {
    public static ModelProperty<ShowBlock.ShowBlockEntity> BASE_BLOCK_ENTITY = new ModelProperty<>();

    @Override
    public ForgeModelDataMapper getModelData(BlockAndTintGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return new ForgeModelData(blockEntity).build((ShowBlock.ShowBlockEntity) blockEntity, BASE_BLOCK_ENTITY);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, ForgeModelDataMapper forgeModelDataMapper) {
        ShowBlock.ShowBlockEntity blockEntity = forgeModelDataMapper.get(BASE_BLOCK_ENTITY);
        if (blockEntity == null) {
            return Collections.emptyList();
        }
        return super.getQuads(state, side, blockEntity);
    }
}