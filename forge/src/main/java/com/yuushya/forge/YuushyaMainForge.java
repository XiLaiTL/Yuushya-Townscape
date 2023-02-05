package com.yuushya.forge;

import com.yuushya.YuushyaMain;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(YuushyaMain.MOD_ID)
public class YuushyaMainForge {
    public YuushyaMainForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(YuushyaForgeRegistry.class);
    }

    private static class YuushyaForgeRegistry {
        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
        }
    }
}