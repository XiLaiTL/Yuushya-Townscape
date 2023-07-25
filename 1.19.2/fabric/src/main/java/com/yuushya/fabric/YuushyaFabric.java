package com.yuushya.fabric;

import com.yuushya.fabriclike.YuushyaFabricLike;
import net.fabricmc.api.ModInitializer;

public class YuushyaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YuushyaFabricLike.init();
    }
}
