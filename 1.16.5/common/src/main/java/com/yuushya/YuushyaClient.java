package com.yuushya;

import com.yuushya.collision.CollisionFileReader;
import com.yuushya.particle.YuushyaParticleFactory;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.utils.YuushyaLogger;
import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.registry.ParticleProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResultHolder;

public class YuushyaClient {
    private static boolean openOnce = true;
    public static void onInitializeClient(){
//        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES,new YuushyaResourceReloadListener());

        YuushyaRegistries.registerClient();

        ClientLifecycleEvent.CLIENT_STARTED.register((client)->{ //client render thread
            CollisionFileReader.readAllCollision();
        });
        /*
        GuiEvent.SET_SCREEN.register(screen -> {
            if(openOnce && screen instanceof SelectWorldScreen) {
                openOnce = false;
                return InteractionResultHolder.success(
                        new ConfirmScreen((b)-> {
                            Minecraft.getInstance().setScreen(screen);},new TranslatableComponent("menu.yuushya.confirm.title"),new TranslatableComponent("menu.yuushya.confirm.description"))
                );
            } else{
                return InteractionResultHolder.pass(screen);
            }
        });
         */

//        YuushyaRegistryConfig.YuushyaRawParticleMap.values().forEach((e)->{
//            ParticleProviderRegistry.register((ParticleType<SimpleParticleType>)YuushyaRegistries.PARTICLE_TYPES.get(e.name).get(), (spriteProvider)-> YuushyaParticleFactory.create(e,spriteProvider));
//        });

    }


}
