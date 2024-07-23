package com.yuushya.forge;

import com.yuushya.Yuushya;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Yuushya.MOD_ID)
public class YuushyaForge {
    public YuushyaForge() {
        // Submit our event bus to let architectury register our content on the right time
        try{
            if(EventBuses.getModEventBus(Yuushya.MOD_ID).isEmpty())
                EventBuses.registerModEventBus(Yuushya.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        } catch (IllegalStateException ignored) {}
        Yuushya.init();
    }
}
