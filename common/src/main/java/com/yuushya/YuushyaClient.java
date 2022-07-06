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

public class YuushyaClient {
    public static void onInitializeClient(){
        RenderTypeRegistry.register(RenderType.cutout(), YuushyaRegistries.SHOW_BLOCK.get());
//        BlockEntityRendererRegistry.register((BlockEntityType<ShowBlockEntity>) YuushyaRegistries.SHOW_BLOCK_ENTITY.get(), ShowBlockEntityRender::new);
        for (String s:List.of("rot_trans_item","pos_trans_item","micro_pos_trans_item","get_showblock_item"))
            ItemPropertiesRegistry.register(YuushyaRegistries.ITEMS.get(s).get(),new ResourceLocation("direction"),(itemStack, clientWorld, livingEntity,i) -> itemStack.hasTag() ? itemStack.getTag().getFloat("TransDirection")*0.1F : 0);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES,new YuushyaResourceReloadListener());
        //        ColorHandlerRegistry.registerBlockColors((state, view, pos, tintIndex) -> {
//                    if (tintIndex > -1) {
//                        // decodeTintWithState
//                        // 假设原tint为负数，则最高位为1，通常可以返回空气（因为不太可能出现上千万的方块状态），那么空气也不会被染色
//                        BlockState trueState = Block.stateById(tintIndex >> 8);
//                        int trueTint = tintIndex & 0xFF;
//                        return ;
//                    } else {
//                        return 0xFFFFFFFF;
//                    }
//                },
//                YuushyaRegistries.SHOW_BLOCK.get());

    }


}