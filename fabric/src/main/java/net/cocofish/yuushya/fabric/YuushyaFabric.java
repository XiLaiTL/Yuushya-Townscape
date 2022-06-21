package net.cocofish.yuushya.fabric;

import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.item.showblocktool.GetBlockStateItem;
import net.cocofish.yuushya.registries.YuushyaRegistries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.Item;

public class YuushyaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YuushyaRegistries.ITEMS.register("get_blockstate_item", () -> new GetBlockStateItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB), 1));
        Yuushya.init();
    }
}
