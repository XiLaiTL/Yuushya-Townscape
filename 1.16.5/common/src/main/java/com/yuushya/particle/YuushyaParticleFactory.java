package com.yuushya.particle;

import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;

public class YuushyaParticleFactory {

    public static <T extends ParticleOptions> ParticleProvider<T> create(YuushyaRegistryData.Particle particle,SpriteSet spriteSet){
        return  (particleOptions,clientLevel,d,e,f,g,h,i)->{switch (particle.classType){
            case "LeafParticle": return new LeafParticle(clientLevel,d,e,f,g,h,i,spriteSet);
            default : return new LeafParticle(clientLevel,d,e,f,g,h,i,spriteSet);
        }};
    }
}
