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

    public static final CreativeModeTab YUUSHYA_EXTRA_BLOCKS = YuushyaCreativeModeTab.create("extra_blocks", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("yellow_acrylic", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_WOOD = YuushyaCreativeModeTab.create("wood", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("raw_oak_wood", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_STONE = YuushyaCreativeModeTab.create("stone", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("park_tile", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_EXTRA_SHAPES = YuushyaCreativeModeTab.create("extra_shapes", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("orange_tile_with_ridge_lower", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_FURNITURE = YuushyaCreativeModeTab.create("furniture", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("classroom_chair", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_LIGHTING = YuushyaCreativeModeTab.create("lighting",()->YuushyaRegistries.ITEMS.getInstanceOrDefault("desk_lamp_0", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_ELECTRICAL_APPLIANCE = YuushyaCreativeModeTab.create("electrical_appliance",()->YuushyaRegistries.ITEMS.getInstanceOrDefault("air_conditioning_condenser", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_LIVING_BEING = YuushyaCreativeModeTab.create("living_being", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("potted_jungle_sprout", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_SIGNS = YuushyaCreativeModeTab.create("signs", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("sign_12", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_CATERING = YuushyaCreativeModeTab.create("catering", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("bottles", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_STORE = YuushyaCreativeModeTab.create("store", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("goods_shelf_jam", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_INFRASTRUCTURE = YuushyaCreativeModeTab.create("infrastructure", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("crash_barrel", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_TRAFFIC = YuushyaCreativeModeTab.create("traffic", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("electric_motorcycle", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_ITEM = YuushyaCreativeModeTab.create("item", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("pos_trans_item", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_STRUCTURE = YuushyaCreativeModeTab.create("structure", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("maple_tree", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_TEMPLATE = YuushyaCreativeModeTab.create("template",()->YuushyaRegistries.ITEMS.getInstanceOrDefault("ridge", Items.APPLE));

    private YuushyaCreativeModeTab() {}
    public static CreativeModeTab create(String name, Supplier<Item> item){
        return CreativeTabRegistry.create(new ResourceLocation(Yuushya.MOD_ID,name),()->new ItemStack(item.get()));
    }

    public static ItemLike getBlueprint(String CreativeModeTab){
        return switch (CreativeModeTab) {
            case "yuushya_extrablocks" -> YuushyaRegistries.ITEMS.get("block_blueprint").get();
            case "yuushya_wood" -> YuushyaRegistries.ITEMS.get("block_blueprint").get();
            case "yuushya_stone" -> YuushyaRegistries.ITEMS.get("block_blueprint").get();
            case "yuushya_furniture" -> YuushyaRegistries.ITEMS.get("furniture_blueprint").get();
            case "yuushya_lighting" -> YuushyaRegistries.ITEMS.get("furniture_blueprint").get();
            case "yuushya_electrical_appliance" -> YuushyaRegistries.ITEMS.get("furniture_blueprint").get();
            case "yuushya_living_being" -> YuushyaRegistries.ITEMS.get("deco_blueprint").get();
            case "yuushya_signs" -> YuushyaRegistries.ITEMS.get("sign_blueprint").get();
            case "yuushya_catering" -> YuushyaRegistries.ITEMS.get("dailylife_stuff_blueprint").get();
            case "yuushya_store" -> YuushyaRegistries.ITEMS.get("dailylife_stuff_blueprint").get();
            case "yuushya_extrashapes" -> YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
            case "yuushya_infrastructure" -> YuushyaRegistries.ITEMS.get("facility_blueprint").get();
            case "yuushya_traffic" -> YuushyaRegistries.ITEMS.get("facility_blueprint").get();
            //case "yuushya_structure" -> YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
            default -> YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
        };
    }

    public static CreativeModeTab toGroup(String CreativeModeTab) {
        return switch (CreativeModeTab) {
            case "yuushya_extrablocks" -> YuushyaCreativeModeTab.YUUSHYA_EXTRA_BLOCKS;
            case "yuushya_wood" -> YuushyaCreativeModeTab.YUUSHYA_WOOD;
            case "yuushya_stone" -> YuushyaCreativeModeTab.YUUSHYA_STONE;
            case "yuushya_furniture" -> YuushyaCreativeModeTab.YUUSHYA_FURNITURE;
            case "yuushya_lighting" -> YuushyaCreativeModeTab.YUUSHYA_LIGHTING;
            case "yuushya_catering" -> YuushyaCreativeModeTab.YUUSHYA_CATERING;
            case "yuushya_electrical_appliance" -> YuushyaCreativeModeTab.YUUSHYA_ELECTRICAL_APPLIANCE;
            case "yuushya_signs" -> YuushyaCreativeModeTab.YUUSHYA_SIGNS;
            case "yuushya_living_being" -> YuushyaCreativeModeTab.YUUSHYA_LIVING_BEING;
            case "yuushya_store" -> YuushyaCreativeModeTab.YUUSHYA_STORE;
            case "yuushya_extrashapes" -> YuushyaCreativeModeTab.YUUSHYA_EXTRA_SHAPES;
            case "yuushya_infrastructure" -> YuushyaCreativeModeTab.YUUSHYA_INFRASTRUCTURE;
            case "yuushya_traffic" -> YuushyaCreativeModeTab.YUUSHYA_TRAFFIC;
            case "yuushya_structure" -> YuushyaCreativeModeTab.YUUSHYA_STRUCTURE;
            case "yuushya_template" -> YuushyaCreativeModeTab.YUUSHYA_TEMPLATE;
            default -> YuushyaCreativeModeTab.YUUSHYA_ITEM;
        };
    }
}
