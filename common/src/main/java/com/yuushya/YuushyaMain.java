package com.yuushya;

import net.minecraft.world.level.block.Block;

public class YuushyaMain {
    public static final String MOD_ID = "yuushya";

    public static void init() {
    }
    @FunctionalInterface
    public interface RegisterBlockItem {
        void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
    }
}
