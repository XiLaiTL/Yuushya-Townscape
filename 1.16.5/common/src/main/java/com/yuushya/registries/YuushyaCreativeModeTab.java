package com.yuushya.registries;

import com.yuushya.Yuushya;
import me.shedaniel.architectury.registry.CreativeTabs;
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
    public static final CreativeModeTab YUUSHYA_WINDOW = YuushyaCreativeModeTab.create("window", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("white_windows_diagonal", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_FURNITURE = YuushyaCreativeModeTab.create("furniture", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("classroom_chair", Items.APPLE));
    public static final CreativeModeTab YUUSHYA_FABRIC = YuushyaCreativeModeTab.create("fabric", ()->YuushyaRegistries.ITEMS.getInstanceOrDefault("hanged_cloth", Items.APPLE));
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
        return CreativeTabs.create(new ResourceLocation(Yuushya.MOD_ID,name),()->new ItemStack(item.get()));
    }

    public static ItemLike getBlueprint(String CreativeModeTab){
        switch (CreativeModeTab) {
            case "yuushya_extrablocks":
                return YuushyaRegistries.ITEMS.get("block_blueprint").get();
            case "yuushya_wood":
                return YuushyaRegistries.ITEMS.get("block_blueprint").get();
            case "yuushya_stone":
                return YuushyaRegistries.ITEMS.get("block_blueprint").get();
            case "yuushya_fabric":
                return YuushyaRegistries.ITEMS.get("fabric_blueprint").get();
            case "yuushya_furniture":
                return YuushyaRegistries.ITEMS.get("furniture_blueprint").get();
            case "yuushya_lighting":
                return YuushyaRegistries.ITEMS.get("lighting_blueprint").get();
            case "yuushya_electrical_appliance":
                return YuushyaRegistries.ITEMS.get("lighting_blueprint").get();
            case "yuushya_living_being":
                return YuushyaRegistries.ITEMS.get("living_being_blueprint").get();
            case "yuushya_signs":
                return YuushyaRegistries.ITEMS.get("sign_blueprint").get();
            case "yuushya_catering":
                return YuushyaRegistries.ITEMS.get("catering_blueprint").get();
            case "yuushya_store":
                return YuushyaRegistries.ITEMS.get("store_blueprint").get();
            case "yuushya_extrashapes":
                return YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
            case "yuushya_window":
                return YuushyaRegistries.ITEMS.get("window_blueprint").get();
            case "yuushya_infrastructure":
                return YuushyaRegistries.ITEMS.get("facility_blueprint").get();
            case "yuushya_traffic":
                return YuushyaRegistries.ITEMS.get("traffic_blueprint").get();
            default:
                return YuushyaRegistries.ITEMS.get("extra_shapes_blueprint").get();
        }
    }

    public static CreativeModeTab toGroup(String CreativeModeTab) {
        switch (CreativeModeTab) {
            case "yuushya_extrablocks":
                return YuushyaCreativeModeTab.YUUSHYA_EXTRA_BLOCKS;
            case "yuushya_wood":
                return YuushyaCreativeModeTab.YUUSHYA_WOOD;
            case "yuushya_stone":
                return YuushyaCreativeModeTab.YUUSHYA_STONE;
            case "yuushya_furniture":
                return YuushyaCreativeModeTab.YUUSHYA_FURNITURE;
            case "yuushya_fabric":
                return YuushyaCreativeModeTab.YUUSHYA_FABRIC;
            case "yuushya_lighting":
                return YuushyaCreativeModeTab.YUUSHYA_LIGHTING;
            case "yuushya_catering":
                return YuushyaCreativeModeTab.YUUSHYA_CATERING;
            case "yuushya_electrical_appliance":
                return YuushyaCreativeModeTab.YUUSHYA_ELECTRICAL_APPLIANCE;
            case "yuushya_signs":
                return YuushyaCreativeModeTab.YUUSHYA_SIGNS;
            case "yuushya_living_being":
                return YuushyaCreativeModeTab.YUUSHYA_LIVING_BEING;
            case "yuushya_store":
                return YuushyaCreativeModeTab.YUUSHYA_STORE;
            case "yuushya_extrashapes":
                return YuushyaCreativeModeTab.YUUSHYA_EXTRA_SHAPES;
            case "yuushya_window":
                return YuushyaCreativeModeTab.YUUSHYA_WINDOW;
            case "yuushya_infrastructure":
                return YuushyaCreativeModeTab.YUUSHYA_INFRASTRUCTURE;
            case "yuushya_traffic":
                return YuushyaCreativeModeTab.YUUSHYA_TRAFFIC;
            case "yuushya_structure":
                return YuushyaCreativeModeTab.YUUSHYA_STRUCTURE;
            case "yuushya_template":
                return YuushyaCreativeModeTab.YUUSHYA_TEMPLATE;
            default:
                return YuushyaCreativeModeTab.YUUSHYA_ITEM;
        }
    }
}
