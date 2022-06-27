package com.yuushya;

import com.google.common.base.Suppliers;
import com.yuushya.datagen.VariantBuilderImpl;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.Registries;
import com.yuushya.registries.YuushyaRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.function.Supplier;

public class Yuushya {
    public static final String MOD_ID = "yuushya";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    public static final CreativeModeTab EXAMPLE_TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "example_tab"), () ->
            new ItemStack(Items.GLOW_ITEM_FRAME));

    public static void init() {

        YuushyaRegistries.registerAll();
        new VariantBuilderImpl().setIdentifier(new ResourceLocation(MOD_ID,"test")).setVariants("").register();
        System.out.println(YuushyaExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
