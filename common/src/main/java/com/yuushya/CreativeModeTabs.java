package com.yuushya;

import com.yuushya.mappings.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface CreativeModeTabs {
    class Wrapper {
        public final ResourceLocation resourceLocation;
        private final Supplier<CreativeModeTab> creativeModeTabSupplier;
        private CreativeModeTab creativeModeTab;

        public Wrapper(ResourceLocation resourceLocation, Supplier<ItemStack> itemSupplier) {
            this.resourceLocation = resourceLocation;
            creativeModeTabSupplier = Registry.getCreativeModeTab(resourceLocation, itemSupplier);
        }

        public CreativeModeTab get() {
            if (creativeModeTab == null) {
                creativeModeTab = creativeModeTabSupplier.get();
            }
            return creativeModeTab;
        }

        public Wrapper() {
            resourceLocation = new ResourceLocation("");
            creativeModeTabSupplier = Utilities::getDefaultTab;
        }
    }
}