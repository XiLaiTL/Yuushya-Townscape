package net.cocofish.yuushya.forge;

import net.cocofish.yuushya.YuushyaExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class YuushyaExpectPlatformImpl {
    /**
     * This is our actual method to {@link YuushyaExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
