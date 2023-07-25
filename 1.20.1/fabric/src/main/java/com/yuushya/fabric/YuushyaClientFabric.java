package com.yuushya.fabric;

import com.yuushya.fabriclike.YuushyaClientFabricLike;
import net.fabricmc.api.ClientModInitializer;

public class YuushyaClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YuushyaClientFabricLike.onInitializeClient();
    }
}