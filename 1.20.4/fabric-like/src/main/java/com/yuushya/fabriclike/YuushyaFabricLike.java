package com.yuushya.fabriclike;

import com.yuushya.Yuushya;

public class YuushyaFabricLike {
    public static void init(){
        Yuushya.init();
        //ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> CollisionFileReader.readAllFileSelf());
    }
}
