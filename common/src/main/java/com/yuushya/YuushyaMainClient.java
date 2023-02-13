package com.yuushya;

import net.minecraft.client.renderer.RenderType;

public class YuushyaMainClient {
    public static void init() {
        RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.SHOW_BLOCK.get());

        RegistryClient.registerBlockColors(Blocks.SHOW_BLOCK.get());
    }

    public static void initItemModelPredicate() {
    }
}