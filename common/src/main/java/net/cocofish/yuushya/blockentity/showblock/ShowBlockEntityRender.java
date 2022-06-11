package net.cocofish.yuushya.blockentity.showblock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class ShowBlockEntityRender implements BlockEntityRenderer<ShowBlockEntity> {

    public ShowBlockEntityRender(BlockEntityRendererProvider.Context context){}

    private final Random random = new Random();
    @Override
    public void render(ShowBlockEntity blockEntity, float tickDelta, PoseStack matrixStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        BlockPos blockPos = blockEntity.getBlockPos();
        blockEntity.getTransformDatas().forEach((transformData)->{
            if(transformData.isShown){
                matrixStack.pushPose();{
                    scale(matrixStack, transformData.scales);
                    translate(matrixStack,transformData.pos);
                    rotate(matrixStack,transformData.rot);
                    BlockState blockState = transformData.blockState;
                    BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
                   // blockRenderDispatcher.renderBatched(blockState,blockPos,blockEntity.getLevel(),matrixStack,multiBufferSource.getBuffer(RenderType.cutout()),false,random);
                    //blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), blockRenderDispatcher.getBlockModel(blockState), blockState, blockPos, matrixStack, multiBufferSource.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)), false, random, blockState.getSeed(blockPos), OverlayTexture.NO_OVERLAY);
                }matrixStack.popPose();
            }
        });
    }
    public static void scale(PoseStack arg,Vector3f scales){
        if(scales.x()!=1||scales.y()!=1||scales.z()!=1)
            arg.scale(scales.x(),scales.y(),scales.z());
    }
    public static void translate(PoseStack arg, Vector3d pos){
        if (pos.x!=0.0||pos.y!=0.0||pos.z!=0.0)
            arg.translate(pos.x/16,pos.y/16,pos.z/16);
    }
    public static void rotate(PoseStack arg,Vector3f rot) {
        float roll = rot.z(),yaw = rot.y(),pitch= rot.x();
        if(roll!=0.0F||yaw != 0.0F||pitch != 0.0F){
            arg.translate(0.5,0.5,0.5);
            if (roll != 0.0F)
                arg.mulPose(Vector3f.ZP.rotationDegrees(roll));
            if (yaw != 0.0F)
                arg.mulPose(Vector3f.YP.rotationDegrees(yaw));
            if (pitch != 0.0F)
                arg.mulPose(Vector3f.XP.rotationDegrees(pitch));
            arg.translate(-0.5,-0.5,-0.5);
        }
    }

}
