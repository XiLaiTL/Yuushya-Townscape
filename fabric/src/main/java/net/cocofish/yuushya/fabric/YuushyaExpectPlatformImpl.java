package net.cocofish.yuushya.fabric;

import net.cocofish.yuushya.YuushyaExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class YuushyaExpectPlatformImpl {
    /**
     * This is our actual method to {@link YuushyaExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
