package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.YuushyaMethod;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MixedBlockEntityRender extends BlockEntityRenderer<MixedBlockEntity> {

    public MixedBlockEntityRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }
    private YuushyaMethod YM=new YuushyaMethod();
    @Override
    public void render(MixedBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        //ItemRenderer itemRenderer=MinecraftClient.getInstance().getItemRenderer();

        int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
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
        blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState1), blockState1, blockPos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState1)), false, new Random(), blockState.getRenderingSeed(blockEntity.getPos()), OverlayTexture.DEFAULT_UV);
        matrices.pop();
        matrices.push();
        matrices.translate(blockEntity.getShowX()/16,blockEntity.getShowY()/16,blockEntity.getShowZ()/16);
        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(blockEntity.getShowPitch()));//V
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(blockEntity.getShowYaw()));//H
//        double a1=-YM.fixZFromQuaternionVector3f(blockEntity.getShowYaw());
//        double b1=0;
//        double c1=YM.fixXFromQuaternionVector3f(blockEntity.getShowYaw());
//        double a2=0;
//        double b2=YM.fixXFromQuaternionVector3f(blockEntity.getShowPitch());
//        double c2=-YM.fixZFromQuaternionVector3f(blockEntity.getShowPitch());
//        Vector3f v1=new Vector3f((float) a1,(float)b1,(float)c1);
//        Vector3f v2=new Vector3f((float)a2,(float)b2,(float)c2);
//        v1.cross(v2);
//        if(blockEntity.getShowPitch()==0) matrices.translate(a1,b1,c1);//Yaw H
//        else if(blockEntity.getShowYaw()==0) matrices.translate(a2,b2,c2);//Pitch V
//        else matrices.translate(a2+a1,b2+b1,c2+c1);
//        else matrices.translate(v1.getX(), v1.getY(),v1.getZ());
     //   matrices.translate(YM.fixXFromQuaternionVector3f(blockEntity.getShowYaw(),blockEntity.getShowPitch()),YM.fixYFromQuaternionVector3f(blockEntity.getShowYaw(),blockEntity.getShowPitch()),YM.fixZFromQuaternionVector3f(blockEntity.getShowYaw(),blockEntity.getShowPitch()));
        Vector3d v=YM.fixFromQuaternion(blockEntity.getShowYaw(),blockEntity.getShowPitch());
        matrices.translate(v.x,v.y,v.z);
        blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockPos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, new Random(), blockState.getRenderingSeed(blockEntity.getPos()), OverlayTexture.DEFAULT_UV);
        matrices.pop();


    }


}
