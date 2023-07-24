package com.yuushya;

import com.yuushya.collision.CollisionFileReader;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.utils.YuushyaLogger;
import me.shedaniel.architectury.event.events.LifecycleEvent;

public class Yuushya {
    public static final String MOD_ID = "yuushya";

    public static void init() {

//        ReloadListenerRegistry.register(PackType.SERVER_DATA,new CollisionFileReadReloadListener());
        YuushyaRegistryConfig.readRegistryConfig();
        YuushyaRegistries.registerRegistries();
        YuushyaRegistries.registerAll();

        CollisionFileReader.readAllFileFromConfig();
        CollisionFileReader.readAllFileSelf();
//        CollisionNetWorkChannel.register();

        LifecycleEvent.SERVER_STARTED.register((server)->{ //server thread
            YuushyaLogger.info("testserver");
            CollisionFileReader.readAllFileFromConfig();
            CollisionFileReader.readAllFileSelf();
            CollisionFileReader.readAllCollision();
        });
//        LifecycleEvent.SERVER_STOPPED.register((server)->{
//            YuushyaBlockFactory.getYuushyaVoxelShapes().clear();
//        });

//        PlayerEvent.PLAYER_JOIN.register((player)->{
//            CollisionNetWorkChannel.sendAllToClient(player);
//        });
        YuushyaLogger.info("Yuushya has been loaded successfully!");


    }
}
