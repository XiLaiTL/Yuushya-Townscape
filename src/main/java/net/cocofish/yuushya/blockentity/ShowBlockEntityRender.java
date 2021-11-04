package net.cocofish.yuushya.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ShowBlockEntityRender extends BlockEntityRenderer<ShowBlockEntity> {

    public ShowBlockEntityRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ShowBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        //ItemRenderer itemRenderer=MinecraftClient.getInstance().getItemRenderer();

        int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
        matrices.push();
        //matrices.translate(-0.5D, 0.0D, -0.5D);
        //matrices.translate(0.50D, 1.25D, 0.5D);
        //matrices.scale(0.5F, 0.5F, 0.5F);
        //ItemStack itemStack = blockEntity.getStack(0);
            //itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
        World world=blockEntity.getWorld();
        BlockState blockState=blockEntity.getBlock(0);
        BlockPos blockPos=blockEntity.getPos();
        matrices.translate(blockEntity.getShowX()/16,blockEntity.getShowY()/16,blockEntity.getShowZ()/16);
        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(blockEntity.getShowPitch()));
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(blockEntity.getShowYaw()));
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockPos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, new Random(), blockState.getRenderingSeed(blockEntity.getPos()), OverlayTexture.DEFAULT_UV);

        matrices.pop();


    }


}
