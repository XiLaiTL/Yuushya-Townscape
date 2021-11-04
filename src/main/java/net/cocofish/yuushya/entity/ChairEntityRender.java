package net.cocofish.yuushya.entity;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class ChairEntityRender extends EntityRenderer<ChairEntity> {
    private static final Identifier TEXTURE = new Identifier("minecraft","textures/entity/minecart.png");
    protected final EntityModel<ChairEntity> model = new MinecartEntityModel();

    public ChairEntityRender(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
        this.shadowRadius = 0.7F;
    }

    public void render(ChairEntity abstractMinecartEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(abstractMinecartEntity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        long l = (long)abstractMinecartEntity.getEntityId() * 493286711L;
        l = l * l * 4392167121L + l * 98761L;
        float h = (((float)(l >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float j = (((float)(l >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float k = (((float)(l >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate((double)h, (double)j, (double)k);
        double d = MathHelper.lerp((double)g, abstractMinecartEntity.lastRenderX, abstractMinecartEntity.getX());
        double e = MathHelper.lerp((double)g, abstractMinecartEntity.lastRenderY, abstractMinecartEntity.getY());
        double m = MathHelper.lerp((double)g, abstractMinecartEntity.lastRenderZ, abstractMinecartEntity.getZ());
        double n = 0.30000001192092896D;
        Vec3d vec3d = abstractMinecartEntity.snapPositionToRail(d, e, m);
        float o = MathHelper.lerp(g, abstractMinecartEntity.prevPitch, abstractMinecartEntity.pitch);
        if (vec3d != null) {
            Vec3d vec3d2 = abstractMinecartEntity.snapPositionToRailWithOffset(d, e, m, 0.30000001192092896D);
            Vec3d vec3d3 = abstractMinecartEntity.snapPositionToRailWithOffset(d, e, m, -0.30000001192092896D);
            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }

            if (vec3d3 == null) {
                vec3d3 = vec3d;
            }

            matrixStack.translate(vec3d.x - d, (vec3d2.y + vec3d3.y) / 2.0D - e, vec3d.z - m);
            Vec3d vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
            if (vec3d4.length() != 0.0D) {
                vec3d4 = vec3d4.normalize();
                f = (float)(Math.atan2(vec3d4.z, vec3d4.x) * 180.0D / 3.141592653589793D);
                o = (float)(Math.atan(vec3d4.y) * 73.0D);
            }
        }

        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - f));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-o));
        float p = (float)abstractMinecartEntity.getDamageWobbleTicks() - g;
        float q = abstractMinecartEntity.getDamageWobbleStrength() - g;
        if (q < 0.0F) {
            q = 0.0F;
        }

        if (p > 0.0F) {
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(MathHelper.sin(p) * p * q / 10.0F * (float)abstractMinecartEntity.getDamageWobbleSide()));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.setAngles(abstractMinecartEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(abstractMinecartEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

    public Identifier getTexture(ChairEntity abstractMinecartEntity) {
        return TEXTURE;
    }

}

