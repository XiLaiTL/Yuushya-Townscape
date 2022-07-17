package com.yuushya;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.yuushya.datagen.BlockStateData;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import com.yuushya.registries.YuushyaResourceReloadListener;
import com.yuushya.utils.YuushyaLogger;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.Registries;
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

    public static void init() {

        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES,new YuushyaResourceReloadListener());
        YuushyaRegistryConfig.readRegistryConfig();
        YuushyaRegistries.registerRegistries();
        YuushyaRegistries.registerAll();
        YuushyaLogger.info("Yuushya has been loaded successfully!");
        System.out.println(YuushyaExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
