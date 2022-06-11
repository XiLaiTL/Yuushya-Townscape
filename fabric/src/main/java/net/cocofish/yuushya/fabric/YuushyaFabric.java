package net.cocofish.yuushya.fabric;

import net.cocofish.yuushya.Yuushya;
import net.fabricmc.api.ModInitializer;

public class YuushyaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Yuushya.init();
    }
}
