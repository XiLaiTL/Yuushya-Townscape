package com.yuushya;

import com.yuushya.registries.YuushyaRegistries;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import com.yuushya.registries.YuushyaResourceReloadListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.util.List;

import static com.yuushya.registries.YuushyaRegistries.BLOCKS;

public class YuushyaClient {
    public static void onInitializeClient(){
        RenderTypeRegistry.register(RenderType.cutout(), YuushyaRegistries.SHOW_BLOCK.get());
//        BlockEntityRendererRegistry.register((BlockEntityType<ShowBlockEntity>) YuushyaRegistries.SHOW_BLOCK_ENTITY.get(), ShowBlockEntityRender::new);

        for (String s:List.of("rot_trans_item","pos_trans_item","micro_pos_trans_item","get_showblock_item"))
            ItemPropertiesRegistry.register(YuushyaRegistries.ITEMS.get(s).get(),new ResourceLocation("direction"),(itemStack, clientWorld, livingEntity,i) -> itemStack.hasTag() ? itemStack.getTag().getFloat("TransDirection")*0.1F : 0);
        ItemPropertiesRegistry.register(YuushyaRegistries.ITEMS.get("get_blockstate_item").get(),new ResourceLocation("direction"),(itemStack, clientWorld, livingEntity,i) -> itemStack.hasTag() ? 1 : 0);

    }


}
