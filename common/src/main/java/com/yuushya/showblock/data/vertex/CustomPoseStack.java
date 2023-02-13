package com.yuushya.showblock.data.vertex;

import com.google.common.collect.Queues;
import com.yuushya.showblock.data.math.Matrix3f;
import com.yuushya.showblock.data.math.Matrix4f;
import com.yuushya.showblock.data.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.util.Mth;

import java.util.Deque;

@Environment(EnvType.CLIENT)
public class CustomPoseStack {
    private final Deque<CustomPoseStack.Pose> poseStack = Util.make(Queues.newArrayDeque(), (arrayDeque) -> {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.setIdentity();
        arrayDeque.add(new CustomPoseStack.Pose(matrix4f, matrix3f));
    });

    public CustomPoseStack() {
    }

    public void translate(double d, double e, double f) {
        CustomPoseStack.Pose pose = this.poseStack.getLast();
        pose.pose.multiplyWithTranslation((float) d, (float) e, (float) f);
    }

    public void scale(float f, float g, float h) {
        CustomPoseStack.Pose pose = this.poseStack.getLast();
        pose.pose.multiply(Matrix4f.createScaleMatrix(f, g, h));
        if (f == g && g == h) {
            if (f > 0.0F) {
                return;
            }

            pose.normal.mul(-1.0F);
        }
        float i = 1.0F / f;
        float j = 1.0F / g;
        float k = 1.0F / h;
        float l = Mth.fastInvCubeRoot(i * j * k);
        pose.normal.mul(Matrix3f.createScaleMatrix(l * i, l * j, l * k));
    }

    public void mulPose(Quaternion quaternion) {
        CustomPoseStack.Pose pose = this.poseStack.getLast();
        pose.pose.multiply(quaternion);
        pose.normal.mul(quaternion);
    }

    public void pushPose() {
        CustomPoseStack.Pose pose = this.poseStack.getLast();
        this.poseStack.addLast(new CustomPoseStack.Pose(pose.pose.copy(), pose.normal.copy()));
    }

    public void popPose() {
        this.poseStack.removeLast();
    }

    public CustomPoseStack.Pose last() {
        return this.poseStack.getLast();
    }

    public boolean clear() {
        return this.poseStack.size() == 1;
    }

    public void setIdentity() {
        CustomPoseStack.Pose pose = this.poseStack.getLast();
        pose.pose.setIdentity();
        pose.normal.setIdentity();
    }

    public void mulPoseMatrix(Matrix4f matrix4f) {
        this.poseStack.getLast().pose.multiply(matrix4f);
    }

    @Environment(EnvType.CLIENT)
    public static final class Pose {
        final Matrix4f pose;
        final Matrix3f normal;

        Pose(Matrix4f matrix4f, Matrix3f matrix3f) {
            this.pose = matrix4f;
            this.normal = matrix3f;
        }

        public Matrix4f pose() {
            return this.pose;
        }

        public Matrix3f normal() {
            return this.normal;
        }
    }
}