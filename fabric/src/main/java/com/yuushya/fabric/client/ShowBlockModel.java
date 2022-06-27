package com.yuushya.fabric.client;

import com.yuushya.blockentity.showblock.ShowBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class ShowBlockModel extends com.yuushya.blockentity.showblock.ShowBlockModel implements UnbakedModel,BakedModel, FabricBakedModel {
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        ShowBlockEntity blockEntity=(ShowBlockEntity) blockView.getBlockEntity(pos);
        if (blockEntity==null) return;
        context.fallbackConsumer().accept(new ShowBlockModel() {
            @Override
            public boolean isVanillaAdapter() {
                return true;
            }

            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction side, Random rand) {
                return super.getQuads(blockState,side,rand,blockEntity);
            }
        });
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}
