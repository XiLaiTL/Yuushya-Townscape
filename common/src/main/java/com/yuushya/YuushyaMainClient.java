package com.yuushya;

import net.minecraft.client.renderer.RenderType;

import static com.yuushya.items.AbstractMultiPurposeToolItem.KEY_TRANS_DIRECTION;

public class YuushyaMainClient {
    public static void init() {
        RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.SHOW_BLOCK.get());

        RegistryClient.registerBlockColors(Blocks.SHOW_BLOCK.get());
    }

    public static void initItemModelPredicate() {
        RegistryClient.registerCustomItemModelPredicate("direction", Items.GET_BLOCK_STATE_ITEM.get(), KEY_TRANS_DIRECTION);
        RegistryClient.registerItemModelPredicate("direction", Items.GET_SHOW_BLOCK_ENTITY_ITEM.get(), KEY_TRANS_DIRECTION);
        RegistryClient.registerItemModelPredicate("direction", Items.POS_TRANS_ITEM.get(), KEY_TRANS_DIRECTION);
        RegistryClient.registerItemModelPredicate("direction", Items.ROT_TRANS_ITEM.get(), KEY_TRANS_DIRECTION);
        RegistryClient.registerItemModelPredicate("direction", Items.SCALE_TRANS_ITEM.get(), KEY_TRANS_DIRECTION);
        RegistryClient.registerItemModelPredicate("direction", Items.MICRO_POS_TRANS_ITEM.get(), KEY_TRANS_DIRECTION);
    }
}