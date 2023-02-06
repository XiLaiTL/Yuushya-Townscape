package com.yuushya.client;

import com.yuushya.blocks.ShowBlock;
import com.yuushya.models.ShowBlockModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
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

public class ShowBlockModelFabric extends ShowBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
#if MC_VERSION >= "11902"
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
#else
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
#endif
        ShowBlock.TileEntityShowBlock entityShowBlock = (ShowBlock.TileEntityShowBlock) blockView.getBlockEntity(pos);
        if (entityShowBlock == null) {
            return;
        }
        context.fallbackConsumer().accept(new ShowBlockModelFabric() {
            @Override
            public boolean isVanillaAdapter() {
                return true;
            }

            @Override
#if MC_VERSION >= "11902"
            public List<BakedQuad> getQuads(BlockState blockState, Direction direction, RandomSource randomSource) {
#else
            public List<BakedQuad> getQuads(BlockState blockState, Direction direction, Random randomSource) {
#endif
                return super.getQuads(blockState, direction, randomSource, entityShowBlock);
            }
        });
    }

    @Override
#if MC_VERSION >= "11902"
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
#else
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
#endif
    }
}