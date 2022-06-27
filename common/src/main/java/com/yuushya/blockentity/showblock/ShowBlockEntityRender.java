package com.yuushya.blockentity.showblock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static com.yuushya.utils.YuushyaUtils.*;

public class ShowBlockEntityRender implements BlockEntityRenderer<ShowBlockEntity> {

    public ShowBlockEntityRender(BlockEntityRendererProvider.Context context){}

    private final Random random = new Random();
    @Override
    public void render(ShowBlockEntity blockEntity, float tickDelta, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {
        BlockPos blockPos = blockEntity.getBlockPos();
        blockEntity.getTransformDatas().forEach((transformData)->{
            if(transformData.isShown){
                matrixStack.pushPose();{
                    scale(matrixStack, transformData.scales);
                    translate(matrixStack,transformData.pos);
                    rotate(matrixStack,transformData.rot);
                    BlockState blockState = transformData.blockState;
                    BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
                    blockRenderDispatcher.renderBatched(blockState,blockPos,blockEntity.getLevel(),matrixStack,multiBufferSource.getBuffer(RenderType.cutout()),false,random);
                    blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), blockRenderDispatcher.getBlockModel(blockState), blockState, blockPos, matrixStack, multiBufferSource.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)), false, random, blockState.getSeed(blockPos), OverlayTexture.NO_OVERLAY);
                }matrixStack.popPose();
            }
        });
    }


}
