package com.yuushya.client;

import com.yuushya.blocks.ShowBlock;
import com.yuushya.models.ShowBlockModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.Collections;
import java.util.List;
#if MC_VERSION >= "11902"
import net.minecraft.util.RandomSource;
#else
import java.util.Random;
#endif

public class ShowBlockModelForge extends ShowBlockModel implements IForgeBakedModel, BakedModel {
    public static ModelProperty<ShowBlock.TileEntityShowBlock> BASE_BLOCK_ENTITY = new ModelProperty<>();

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        if (level.getBlockEntity(pos) == null) {
            return ModelData.builder().build();
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ShowBlock.TileEntityShowBlock) {
                ShowBlock.TileEntityShowBlock blockEntity1 = (ShowBlock.TileEntityShowBlock) blockEntity;
                return ModelData.builder().with(BASE_BLOCK_ENTITY, blockEntity1).build();
            } else {
                return ModelData.builder().build();
            }
        }
    }

    @Override
#if MC_VERSION >= "11902"
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
#else
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, ModelData extraData, RenderType renderType) {
#endif
        ShowBlock.TileEntityShowBlock blockEntity = extraData.get(BASE_BLOCK_ENTITY);
        if (blockEntity == null) {
            return Collections.emptyList();
        }
        return super.getQuads(state, side, rand, blockEntity);
    }
}
