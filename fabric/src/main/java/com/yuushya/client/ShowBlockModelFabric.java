package com.yuushya.client;

import com.yuushya.mappings.FabricBakedModelMapper;
import com.yuushya.showblock.ShowBlock;
import com.yuushya.showblock.ShowBlockModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ShowBlockModelFabric extends ShowBlockModel implements FabricBakedModelMapper {
    @Override
    public void emitItemQuads(ItemStack stack, RenderContext context) {
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, RenderContext context) {
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
            public List<BakedQuad> getQuads(BlockState blockState, Direction direction) {
                return super.getQuads(blockState, direction, blockEntity);
            }
        });
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }
}