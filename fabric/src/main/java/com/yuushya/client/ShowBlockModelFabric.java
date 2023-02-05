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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public class ShowBlockModelFabric extends ShowBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
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
            public List<BakedQuad> getQuads(BlockState blockState, Direction direction, RandomSource randomSource) {
                return super.getQuads(blockState, direction, randomSource, entityShowBlock);
            }
        });
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {

    }
}
