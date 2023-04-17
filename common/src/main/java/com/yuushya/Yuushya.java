package com.yuushya;

import com.yuushya.collision.CollisionFileReadReloadListener;
import com.yuushya.collision.CollisionFileReader;
import com.yuushya.collision.CollisionNetWorkChannel;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.utils.YuushyaLogger;
import dev.architectury.event.Event;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;

public class Yuushya {
    public static final String MOD_ID = "yuushya";

    public static void init() {

        ReloadListenerRegistry.register(PackType.SERVER_DATA,new CollisionFileReadReloadListener());
        YuushyaRegistryConfig.readRegistryConfig();
        YuushyaRegistries.registerRegistries();
        YuushyaRegistries.registerAll();
        CollisionNetWorkChannel.register();
//        LifecycleEvent.SERVER_STARTED.register((server)->{
//            CollisionFileReader.readAllCollision();
//        });

        PlayerEvent.PLAYER_JOIN.register((player)->{
            CollisionNetWorkChannel.sendAllToClient(player);
        });
        YuushyaLogger.info("Yuushya has been loaded successfully!");


    }
}
