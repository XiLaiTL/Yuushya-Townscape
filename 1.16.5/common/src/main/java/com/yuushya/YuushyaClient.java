package com.yuushya;

import com.yuushya.collision.CollisionFileReader;
import com.yuushya.gui.CheckScreen;
import com.yuushya.registries.YuushyaConfig;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.CheckFileUtils;
import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.InteractionResultHolder;

public class YuushyaClient {
    private static boolean openOnce = true;
    public static void onInitializeClient(){
        YuushyaConfig.CLIENT_CONFIG.read();
//        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES,new YuushyaResourceReloadListener());

        YuushyaRegistries.registerClient();

        ClientLifecycleEvent.CLIENT_STARTED.register((client)->{ //client render thread
            CollisionFileReader.readAllCollision();
        });

        if(YuushyaConfig.CLIENT_CONFIG.check) {
            CheckFileUtils.loadInformation();
            GuiEvent.SET_SCREEN.register(screen -> {
                if (openOnce && screen instanceof TitleScreen) {
                    openOnce = false;
                    return InteractionResultHolder.success(
                            new CheckScreen(screen)
                            //new ConfirmScreen((b) -> {Minecraft.getInstance().setScreen(screen);}, new TranslatableComponent("menu.yuushya.confirm.title"), new TranslatableComponent("menu.yuushya.confirm.description"))
                    );
                } else {
                    return InteractionResultHolder.pass(screen);
                }
            });
        }


//        YuushyaRegistryConfig.YuushyaRawParticleMap.values().forEach((e)->{
//            ParticleProviderRegistry.register((ParticleType<SimpleParticleType>)YuushyaRegistries.PARTICLE_TYPES.get(e.name).get(), (spriteProvider)-> YuushyaParticleFactory.create(e,spriteProvider));
//        });

    }


}
