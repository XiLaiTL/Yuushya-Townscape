package net.cocofish.yuushya.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class YuushyaUtils {

    public static void scale(PoseStack arg, Vector3f scales){
        if(scales.x()!=1||scales.y()!=1||scales.z()!=1){
            arg.translate(0.5,0.5,0.5);
            arg.scale(scales.x(),scales.y(),scales.z());
            arg.translate(-0.5,-0.5,-0.5);
        }
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

    public static int encodeTintWithState(int tint, BlockState state) {
        // 最高位依然可以保留负数信息，但tint的有效位数很低了，原版够用，mod一般也不会用这个东西
        return Block.getId(state) << 8 | tint;
    }

}
