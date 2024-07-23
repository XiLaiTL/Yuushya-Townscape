package com.yuushya.quilt;

import com.yuushya.fabriclike.YuushyaFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;


public class YuushyaQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        YuushyaFabricLike.init();
    }
}
