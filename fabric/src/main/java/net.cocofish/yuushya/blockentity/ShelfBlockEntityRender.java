package net.cocofish.yuushya.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class ShelfBlockEntityRender implements BlockEntityRenderer<ShelfBlockEntity> {
    // 唱片机物品栏位
    private static ItemStack stack = new ItemStack(Items.JUKEBOX, 1);

    public ShelfBlockEntityRender(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(ShelfBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer=MinecraftClient.getInstance().getItemRenderer();
        int seed = (int)blockEntity.getPos().asLong();
//        matrices.push();
        int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
//        matrices.translate(0.5, 1.25, 0.5);
//        ItemStack it=blockEntity.getStack(0).copy();
//        itemRenderer.renderItem(it, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
//        matrices.pop();

//        else
//        {
//            int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
//            matrices.translate(0.5, 1.25, 0.5);
//
//            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
//
//        }
        // Mandatory call after GL calls


        
//        matrices.push();
//
//        Direction direction =blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(HORIZONTAL_FACING);
//        Vec3d vec3d = this.getPositionOffset(blockEntity, tickDelta);
//        matrices.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
//        double d = 0.46875D;
//        matrices.translate((double)direction.getOffsetX() * 0.46875D, (double)direction.getOffsetY() * 0.46875D, (double)direction.getOffsetZ() * 0.46875D);
//        ItemStack itemStack = blockEntity.getStack(0);
//        if (!itemStack.isEmpty()) {
//            matrices.translate(0.0D, 0.0D, 0.4375D);
//            int j = 0;
//            matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float)j * 360.0F / 8.0F));
//            matrices.scale(0.5F, 0.5F, 0.5F);
//            itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
//
//        }
//
//        matrices.pop();

        matrices.push();
        matrices.translate(0.50D, 1.25D, 0.5D);
        matrices.scale(0.5F, 0.5F, 0.5F);
        ItemStack itemStack = blockEntity.getStack(0);
            itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers,seed);

        matrices.pop();

//        matrices.push();
//        BlockState blockState = blockEntity.getStack(0).getItem();
//        BlockPos blockPos = new BlockPos(blockEntity.getX(), blockEntity.getBoundingBox().maxY, blockEntity.getZ());
//        matrices.translate(-0.5D, 0.0D, -0.5D);
//        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
//        blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockPos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, new Random(), blockState.getRenderingSeed(fallingBlockEntity.getFallingBlockPos()), OverlayTexture.DEFAULT_UV);
//        matrices.pop();
    }

    public Vec3d getPositionOffset(ShelfBlockEntity itemFrameEntity, float f) {
        Direction direction =itemFrameEntity.getWorld().getBlockState(itemFrameEntity.getPos()).get(HORIZONTAL_FACING);

        return new Vec3d((double)((float)direction.getOffsetX() * 0.3F), -0.25D, (double)((float)direction.getOffsetZ() * 0.3F));
    }

}
