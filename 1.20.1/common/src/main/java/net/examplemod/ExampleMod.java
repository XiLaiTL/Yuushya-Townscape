package net.examplemod;

import com.google.common.base.Suppliers;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ExampleMod {
    public static final String MOD_ID = "examplemod";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    // Registering a new creative tab
    //public static final CreativeModeTab EXAMPLE_TAB = CreativeTabRegistry.create(Component.literal( "example_tab"), () -> new ItemStack(ExampleMod.EXAMPLE_ITEM.get()));
    public static final Registrar<CreativeModeTab> TABS = REGISTRIES.get().get( Registries.CREATIVE_MODE_TAB);
    public static final Registrar<Item> ITEMS = REGISTRIES.get().get( Registries.ITEM);
    public static RegistrySupplier<CreativeModeTab> EXAMPLE_TAB;
    public static  RegistrySupplier<Item> EXAMPLE_ITEM ;

    public static void init() {
        EXAMPLE_TAB = TABS.register(new ResourceLocation(MOD_ID,"example_tab"),()->CreativeTabRegistry.create(Component.literal("example"),()->new ItemStack(EXAMPLE_ITEM.get())));
        EXAMPLE_ITEM = ITEMS.register(new ResourceLocation(MOD_ID,"example_item"), () -> new Item(new Item.Properties().arch$tab(EXAMPLE_TAB)));

        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}