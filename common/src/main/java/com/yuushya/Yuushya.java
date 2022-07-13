package com.yuushya;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuushya.datagen.BlockStateData;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.registries.YuushyaResourceReloadListener;
import com.yuushya.utils.GsonTools;
import com.yuushya.utils.YuushyaLogger;
import com.yuushya.utils.YuushyaModelUtils;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.Registries;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Supplier;

public class Yuushya {
    public static final String MOD_ID = "yuushya";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    public static final CreativeModeTab EXAMPLE_TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "example_tab"), () ->
            new ItemStack(Items.GLOW_ITEM_FRAME));

    public static void init() {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES,new YuushyaResourceReloadListener());
        YuushyaRegistries.registerAll();
        YuushyaLogger.info("Yuushya has been loaded successfully!");
        YuushyaLogger.info(
                BlockStateData.genBlockState(Blocks.ACACIA_BUTTON, List.of("powered=true#yuushya:a","powered=true,facing=west#yuushya:b")).toString()
        );

        System.out.println(YuushyaExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
