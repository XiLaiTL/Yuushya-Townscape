package net.cocofish.yuushya.blockentity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ModelFrameBlockEntityRender extends BlockEntityRenderer<ModelFrameBlockEntity> {
    // 唱片机物品栏位
    private static ItemStack stack = new ItemStack(Items.JUKEBOX, 1);

    public ModelFrameBlockEntityRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ModelFrameBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer=MinecraftClient.getInstance().getItemRenderer();

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




        DefaultedList<ItemStack> defaultedList =blockEntity.getItems();
        for(int k = 0; k < defaultedList.size(); ++k) {
            ItemStack itemStack = (ItemStack)defaultedList.get(k);
            if (itemStack != ItemStack.EMPTY) {
                matrices.push();
                matrices.translate(0.5D, 0.44921875D, 0.5D);
                Direction direction2 = Direction.fromHorizontal((k ) % 4);
                float g = -direction2.asRotation();
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(g));
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                matrices.translate(-0.3125D, -0.3125D, 0.0D);
                matrices.scale(0.375F, 0.375F, 0.375F);
                itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
                matrices.pop();
            }
            else
            {
                ItemStack stack1 = new ItemStack(Items.AIR, 1);
                matrices.push();
                matrices.translate(0.5D, 0.44921875D, 0.5D);
                Direction direction2 = Direction.fromHorizontal((k ) % 4);
                float g = -direction2.asRotation();
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(g));
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                matrices.translate(-0.3125D, -0.3125D, 0.0D);
                matrices.scale(0.375F, 0.375F, 0.375F);
                itemRenderer.renderItem(stack1, ModelTransformation.Mode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
                matrices.pop();
            }
        }


    }

}
