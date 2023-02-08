package com.yuushya.fabric;

import com.yuushya.YuushyaMainClient;
import net.fabricmc.api.ClientModInitializer;

public class YuushyaMainFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YuushyaMainClient.init();
        YuushyaMainClient.initItemModelPredicate();
    }
}