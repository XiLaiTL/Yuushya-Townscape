package com.yuushya;

import com.yuushya.collision.CollisionFileReader;
import com.yuushya.registries.YuushyaConfig;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.AddonLoader;
import com.yuushya.utils.YuushyaLogger;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;

public class Yuushya {
    public static final String MOD_ID = "yuushya";

    public static void init() {
        AddonLoader.loadResource(MOD_ID,Yuushya.class);
        AddonLoader.loadPackResource(Platform.getModsFolder());
//        YuushyaRegistryConfig.readRegistrySelf();
        AddonLoader.getRegister();
        YuushyaRegistries.registerRegistries();
        YuushyaRegistries.registerAll();

        AddonLoader.getCollision();
//        CollisionFileReader.readAllFileSelf();

        LifecycleEvent.SERVER_STARTED.register((server)->{ //server thread
            AddonLoader.getCollision();
//            CollisionFileReader.readAllFileSelf();
            CollisionFileReader.readAllCollision();
        });

        YuushyaLogger.info("Yuushya has been loaded successfully!");
    }
}
