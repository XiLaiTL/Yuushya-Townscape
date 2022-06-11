package net.cocofish.yuushya.forge;

import dev.architectury.platform.forge.EventBuses;
import net.cocofish.yuushya.Yuushya;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Yuushya.MOD_ID)
public class YuushyaForge {
    public YuushyaForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Yuushya.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Yuushya.init();

    }
}
