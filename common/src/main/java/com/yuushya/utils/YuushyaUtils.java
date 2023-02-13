package com.yuushya.utils;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuushya.showblock.ShowBlock;
import com.yuushya.showblock.data.math.Vector3d;
import com.yuushya.showblock.data.math.Vector3f;
import com.yuushya.showblock.data.vertex.CustomPoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class YuushyaUtils {
    public static int vertexSize() {
        return DefaultVertexFormat.BLOCK.getVertexSize() / 4;
    }

    public static void scale(PoseStack arg, Vector3f scales) {
        if (scales.x() != 1 || scales.y() != 1 || scales.z() != 1) {
            arg.translate(0.5, 0.5, 0.5);
            arg.scale(scales.x(), scales.y(), scales.z());
            arg.translate(-0.5, -0.5, -0.5);
        }
    }

    public static void translate(PoseStack arg, Vector3d pos) {
        if (pos.x != 0.0 || pos.y != 0.0 || pos.z != 0.0) {
            arg.translate(pos.x / 16, pos.y / 16, pos.z / 16);
        }
    }

    public static void rotate(CustomPoseStack arg, Vector3f rot) {
        float roll = rot.z(), yaw = rot.y(), pitch = rot.x();
        if (roll != 0.0F || yaw != 0.0F || pitch != 0.0F) {
            arg.translate(0.5, 0.5, 0.5);
            if (roll != 0.0F) {
                arg.mulPose(Vector3f.ZP.rotationDegrees(roll));
            }
            if (yaw != 0.0F) {
                arg.mulPose(Vector3f.YP.rotationDegrees(yaw));
            }
            if (pitch != 0.0F) {
                arg.mulPose(Vector3f.XP.rotationDegrees(pitch));
            }
            arg.translate(-0.5, -0.5, -0.5);
        }
    }

    public static BlockState getBlockState(BlockState blockState, LevelAccessor world, BlockPos blockPos) {
        if (blockState.getBlock() instanceof ShowBlock) {
            return ((ShowBlock.ShowBlockEntity) world.getBlockEntity(blockPos)).getTransformData(0).blockState;
        } else return blockState;
    }
}