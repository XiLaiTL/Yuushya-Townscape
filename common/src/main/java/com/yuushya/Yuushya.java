package com.yuushya;

import com.yuushya.collision.CollisionFileReadReloadListener;
import com.yuushya.collision.CollisionFileReader;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.utils.YuushyaLogger;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;

public class Yuushya {
    public static final String MOD_ID = "yuushya";

    public static void init() {

        ReloadListenerRegistry.register(PackType.SERVER_DATA,new CollisionFileReadReloadListener());
        YuushyaRegistryConfig.readRegistryConfig();
        YuushyaRegistries.registerRegistries();
        YuushyaRegistries.registerAll();

        CollisionFileReader.readAll();
        YuushyaLogger.info("Yuushya has been loaded successfully!");
    }
}
