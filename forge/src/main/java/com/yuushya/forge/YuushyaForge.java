package com.yuushya.forge;

import com.yuushya.Yuushya;
import com.yuushya.forge.item.GetBlockStateItemForge;
import com.yuushya.registries.YuushyaRegistries;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Yuushya.MOD_ID)
public class YuushyaForge {
    public YuushyaForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Yuushya.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        YuushyaRegistries.ITEMS.register("get_blockstate_item", () -> new GetBlockStateItemForge(new Item.Properties().tab(Yuushya.EXAMPLE_TAB), 1));
        Yuushya.init();
    }
}