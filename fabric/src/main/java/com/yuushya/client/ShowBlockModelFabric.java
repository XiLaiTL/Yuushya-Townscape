package com.yuushya.client;

import com.yuushya.mappings.BakedModelMapper;
import com.yuushya.mappings.UnbakedModelMapper;
import com.yuushya.showblock.ShowBlock;
import com.yuushya.showblock.ShowBlockModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

#if MC_VERSION >= "11902"
import net.minecraft.util.RandomSource;
#else
import java.util.Random;
#endif

public class ShowBlockModelFabric extends ShowBlockModel implements FabricBakedModel, UnbakedModelMapper, BakedModelMapper {
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<#if MC_VERSION >= "11902" RandomSource #else Random #endif> randomSupplier, RenderContext context) {
        ShowBlock.ShowBlockEntity blockEntity = (ShowBlock.ShowBlockEntity) blockView.getBlockEntity(pos);
        if (blockEntity == null) {
            return;
        }
        context.fallbackConsumer().accept(new ShowBlockModelFabric() {
            @Override
            public boolean isVanillaAdapter() {
                return true;
            }

            @Override
            public List<BakedQuad> getQuadsMapper(BlockState blockState, Direction direction) {
                return super.getQuads(blockState, direction, blockEntity);
            }
        });
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<#if MC_VERSION >= "11902" RandomSource #else Random #endif> randomSupplier, RenderContext context) {

    }
}