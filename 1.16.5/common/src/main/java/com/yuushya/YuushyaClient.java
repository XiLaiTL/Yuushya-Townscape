package com.yuushya;

import com.yuushya.collision.CollisionFileReader;
import com.yuushya.particle.YuushyaParticleFactory;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.utils.YuushyaLogger;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.registry.ParticleProviderRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class YuushyaClient {
    public static void onInitializeClient(){
//        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES,new YuushyaResourceReloadListener());

        YuushyaRegistries.registerClient();

        ClientLifecycleEvent.CLIENT_STARTED.register((client)->{ //client render thread
            YuushyaLogger.info("test");
            CollisionFileReader.readAllCollision();
        });


//        YuushyaRegistryConfig.YuushyaRawParticleMap.values().forEach((e)->{
//            ParticleProviderRegistry.register((ParticleType<SimpleParticleType>)YuushyaRegistries.PARTICLE_TYPES.get(e.name).get(), (spriteProvider)-> YuushyaParticleFactory.create(e,spriteProvider));
//        });

    }


}
