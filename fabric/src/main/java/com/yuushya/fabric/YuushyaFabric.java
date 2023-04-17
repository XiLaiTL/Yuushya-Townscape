package com.yuushya.fabric;

import com.yuushya.Yuushya;
import com.yuushya.collision.CollisionFileReader;
import com.yuushya.collision.CollisionNetWorkChannel;
import com.yuushya.item.showblocktool.GetBlockStateItem;
import com.yuushya.registries.YuushyaCreativeModeTab;
import com.yuushya.registries.YuushyaRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.item.Item;

public class YuushyaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YuushyaRegistries.ITEMS.register("get_blockstate_item", () -> new GetBlockStateItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM), 3));
        Yuushya.init();
    }
}
