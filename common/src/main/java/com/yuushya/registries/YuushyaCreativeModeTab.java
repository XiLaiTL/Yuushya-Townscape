package com.yuushya.registries;

import com.yuushya.Yuushya;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class YuushyaCreativeModeTab {
    public static final CreativeModeTab YUUSHYA_EXTRA_SHAPES = YuushyaCreativeModeTab.create("extra_shapes", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("orange_tile_with_ridge_lower", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_FURNITURE = YuushyaCreativeModeTab.create("furniture", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("classroom_chair", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_DECORATION = YuushyaCreativeModeTab.create("decoration", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("potted_jungle_sprout", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_SIGNS = YuushyaCreativeModeTab.create("signs", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("sign_12", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_LIFE = YuushyaCreativeModeTab.create("life", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("bottles", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_EXTRA_BLOCKS = YuushyaCreativeModeTab.create("extra_blocks", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("raw_oak_wood", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_INFRASTRUCTURE = YuushyaCreativeModeTab.create("infrastructure", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("crash_barrel", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_ITEM = YuushyaCreativeModeTab.create("item", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("pos_trans_item", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_STRUCTURE = YuushyaCreativeModeTab.create("structure", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("maple_tree", Items.APPLE));

    private YuushyaCreativeModeTab() {}
    public static CreativeModeTab create(String name, Supplier<Item> item){
        return CreativeTabRegistry.create(new ResourceLocation(Yuushya.MOD_ID,name),()->new ItemStack(item.get()));
    }

    public static ItemLike getBlueprint(String CreativeModeTab){
        return switch (CreativeModeTab) {
            case "yuushya_extrablocks" -> YuushyaRegistries.ITEMS.get("block_blueprint").get();
            case "yuushya_furniture" -> YuushyaRegistries.ITEMS.get("furniture_blueprint").get();
            case "yuushya_decoration" -> YuushyaRegistries.ITEMS.get("deco_blueprint").get();
            case "yuushya_signs" -> YuushyaRegistries.ITEMS.get("sign_blueprint").get();
            case "yuushya_life" -> YuushyaRegistries.ITEMS.get("dailylife_stuff_blueprint").get();
            case "yuushya_extrashapes" -> YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
            case "yuushya_infrastructure" -> YuushyaRegistries.ITEMS.get("facility_blueprint").get();
            //case "yuushya_structure" -> YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
            default -> YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
        };
    }

    public static CreativeModeTab toGroup(String CreativeModeTab) {
        return switch (CreativeModeTab) {
            case "yuushya_extrablocks" -> YuushyaCreativeModeTab.YUUSHYA_EXTRA_BLOCKS;
            case "yuushya_furniture" -> YuushyaCreativeModeTab.YUUSHYA_FURNITURE;
            case "yuushya_decoration" -> YuushyaCreativeModeTab.YUUSHYA_DECORATION;
            case "yuushya_signs" -> YuushyaCreativeModeTab.YUUSHYA_SIGNS;
            case "yuushya_life" -> YuushyaCreativeModeTab.YUUSHYA_LIFE;
            case "yuushya_extrashapes" -> YuushyaCreativeModeTab.YUUSHYA_EXTRA_SHAPES;
            case "yuushya_infrastructure" -> YuushyaCreativeModeTab.YUUSHYA_INFRASTRUCTURE;
            case "yuushya_structure" -> YuushyaCreativeModeTab.YUUSHYA_STRUCTURE;
            default -> YuushyaCreativeModeTab.YUUSHYA_ITEM;
        };
    }
}
