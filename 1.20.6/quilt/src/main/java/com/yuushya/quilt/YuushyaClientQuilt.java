package com.yuushya.quilt;

import com.yuushya.fabriclike.YuushyaClientFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class YuushyaClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        YuushyaClientFabricLike.onInitializeClient();
    }
}