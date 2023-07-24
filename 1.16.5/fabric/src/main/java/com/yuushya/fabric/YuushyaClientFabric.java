package com.yuushya.fabric;

import com.yuushya.YuushyaClient;
import com.yuushya.entity.ChairEntity;
import com.yuushya.entity.ChairEntityRender;
import com.yuushya.particle.YuushyaParticleFactory;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class YuushyaClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YuushyaClient.onInitializeClient();

        EntityRendererRegistry.INSTANCE.register((EntityType<ChairEntity>) YuushyaRegistries.CHAIR_ENTITY.get(), new EntityRendererRegistry.Factory() {
            @Override
            public EntityRenderer<? extends Entity> create(EntityRenderDispatcher manager, EntityRendererRegistry.Context context) {
                return new ChairEntityRender(manager);
            }
        });

        YuushyaRegistryConfig.YuushyaRawParticleMap.values().forEach((e)->{
            ParticleFactoryRegistry.getInstance().register((ParticleType<SimpleParticleType>)YuushyaRegistries.PARTICLE_TYPES.get(e.name).get(), (spriteProvider)-> YuushyaParticleFactory.create(e,spriteProvider));
        });
        //ParticleFactoryRegistry.getInstance().register((ParticleType<SimpleParticleType>) YuushyaRegistries.PARTICLE_TYPES.get("leaf_particle").get(), LeafParticle.Factory::new);
    }
}