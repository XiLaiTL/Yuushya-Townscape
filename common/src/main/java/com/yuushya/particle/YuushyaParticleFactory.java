package com.yuushya.particle;

import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class YuushyaParticleFactory {

    public static <T extends ParticleOptions> ParticleProvider<T> create(YuushyaRegistryData.Particle particle,SpriteSet spriteSet){
        return  (particleOptions,clientLevel,d,e,f,g,h,i)->switch (particle.classType){
            case "LeafParticle"->new LeafParticle(clientLevel,d,e,f,g,h,i,spriteSet);
            default -> new LeafParticle(clientLevel,d,e,f,g,h,i,spriteSet);
        };
    }
}
