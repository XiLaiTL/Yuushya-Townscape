package com.yuushya.fabric;

import com.yuushya.Yuushya;
import net.fabricmc.api.ModInitializer;

public class YuushyaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Yuushya.init();
        //ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> CollisionFileReader.readAllFileSelf());
    }
}
