package com.yuushya.forge.client;


import com.yuushya.blockentity.showblock.ShowBlockEntity;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShowBlockModel extends com.yuushya.blockentity.showblock.ShowBlockModel implements IForgeBakedModel, BakedModel {
    public static ModelProperty<ShowBlockEntity> BASE_BLOCK_ENTITY = new ModelProperty<>();

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData modelData) {
        if (level.getBlockEntity(pos) == null) {
            return new ModelDataMap.Builder().build();
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ShowBlockEntity blockEntity1)
                return new ModelDataMap.Builder().withInitial(BASE_BLOCK_ENTITY, blockEntity1).build();
            else
                return new ModelDataMap.Builder().build();
        }
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        ShowBlockEntity blockEntity=extraData.getData(BASE_BLOCK_ENTITY);
        if (blockEntity==null) return Collections.emptyList();
        return super.getQuads(state,side,rand,blockEntity);
    }
}
