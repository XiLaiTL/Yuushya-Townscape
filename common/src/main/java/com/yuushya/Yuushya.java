package com.yuushya;

import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.utils.YuushyaLogger;

public class Yuushya {
    public static final String MOD_ID = "yuushya";

    public static void init() {

        YuushyaRegistryConfig.readRegistryConfig();
        YuushyaRegistries.registerRegistries();
        YuushyaRegistries.registerAll();
        YuushyaLogger.info("Yuushya has been loaded successfully!");
        System.out.println(YuushyaExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
