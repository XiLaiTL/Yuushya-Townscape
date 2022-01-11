package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.YuushyaUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MixedBlockEntityRender implements BlockEntityRenderer<MixedBlockEntity> {

    public MixedBlockEntityRender(BlockEntityRendererFactory.Context ctx) {}
    private static final YuushyaUtils YM=new YuushyaUtils();
    private final Random random=new Random();
    @Override
    public void render(MixedBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        //ItemRenderer itemRenderer=MinecraftClient.getInstance().getItemRenderer();

        //int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
        //matrices.translate(-0.5D, 0.0D, -0.5D);
        //matrices.transla        matrices.push();te(0.50D, 1.25D, 0.5D);
        //matrices.scale(0.5F, 0.5F, 0.5F);
        //ItemStack itemStack = blockEntity.getStack(0);
            //itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
        World world=blockEntity.getWorld();
        BlockState blockState=blockEntity.getBlock(0);
        BlockState blockState1=blockEntity.getBlock(1);
        BlockPos blockPos=blockEntity.getPos();
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        matrices.push();
        blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState1), blockState1, blockPos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState1)), false, random, blockState.getRenderingSeed(blockEntity.getPos()), OverlayTexture.DEFAULT_UV);
        matrices.pop();
        matrices.push();
        matrices.translate(blockEntity.getShowX()/16,blockEntity.getShowY()/16,blockEntity.getShowZ()/16);
        matrices.translate(0.5,0.5,0.5);
        YuushyaUtils.rotate(matrices, blockEntity.getShowRotationZ(),blockEntity.getShowRotationY(),blockEntity.getShowRotationX());
        matrices.translate(-0.5,-0.5,-0.5);
        blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockPos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, random, blockState.getRenderingSeed(blockEntity.getPos()), OverlayTexture.DEFAULT_UV);

        matrices.pop();


    }


}
