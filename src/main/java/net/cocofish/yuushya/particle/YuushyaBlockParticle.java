package net.cocofish.yuushya.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.awt.*;

public class YuushyaBlockParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    protected YuushyaBlockParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ,SpriteProvider spriteProvider) {
        super(world, x, y, z,velocityX,velocityY,velocityZ);
        maxAge=300;
        this.gravityStrength= 0.03F+random.nextFloat()*0.03F;

        this.velocityX=0.0F;
        this.velocityY=0.0F;
        this.velocityZ=0.0F;
//        this.setPos(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
//        this.setColorAlpha(color.getAlpha());
//        scale=diameter*0.5F;
        //this.angle = this.prevAngle = random.nextFloat() *2* (float)(Math.PI);


        this.spriteProvider = spriteProvider;
        this.scale(1.5F);
        this.collidesWithWorld=true;
        this.setSprite(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        super.tick();
//        if (!this.dead) {
//            this.setSpriteForAge(this.spriteProvider);
//        }
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.prevAngle = this.angle;

        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            this.velocityY -= 0.04D*this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);

            this.velocityY *= 0.9599999785423279D;
            this.velocityX =0.012*Math.sin(this.y*0.2);
            this.velocityZ =0.012*Math.cos(this.y*0.2);

            if (this.y == this.prevPosY) {
                this.velocityX *= 1.1D;
                this.velocityZ *= 1.1D;
            }


            this.angle=(float)Math.sin((double)this.age*0.03)*0.75F;

            if (this.onGround) {
                this.velocityX *= 0.699999988079071D;
                this.velocityZ *= 0.699999988079071D;
            }

        }

    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            YuushyaBlockParticle yuushyaBlockParticle = new YuushyaBlockParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            return yuushyaBlockParticle;
        }
    }
}
