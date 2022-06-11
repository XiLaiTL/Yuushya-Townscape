package net.cocofish.yuushya;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntityRender;
import net.cocofish.yuushya.registries.YuushyaRegistries;
import net.minecraft.client.renderer.RenderType;

public class YuushyaClient {
    public static void onInitializeClient(){
        //RenderTypeRegistry.register(RenderType.cutout(), YuushyaRegistries.SHOW_BLOCK);
        //BlockEntityRendererRegistry.register(YuushyaRegistries.SHOW_BLOCK_ENTITY, ShowBlockEntityRender::new);

        //ItemPropertiesRegistry.register()
    }
}
