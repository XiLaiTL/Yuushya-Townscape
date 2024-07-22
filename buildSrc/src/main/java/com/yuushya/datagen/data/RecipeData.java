package com.yuushya.datagen.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuushya.datagen.ConfigReader;
import com.yuushya.datagen.utils.ResourceLocation;
import com.yuushya.registries.YuushyaRegistryData;

import static com.yuushya.datagen.utils.Utils.MOD_ID;

public class RecipeData {

    public static JsonElement genStoneCutterRecipe(ResourceLocation item, ResourceLocation father,int resultNum){
        //
        return JsonParser.parseString("""
                {"type":"minecraft:stonecutting","ingredient":{"item":"%s"},"result":"%s","count":%s}
                """.formatted(father.toString(),item.toString(),resultNum));
    }

    public static int getResultNumber(String CreativeModeTab){
        return switch (CreativeModeTab){
            case "yuushya_template"->1;
            default -> 4;
        };
    }
    public static YuushyaRegistryData.ItemGroup defaultItemGroup = new YuushyaRegistryData.ItemGroup(){{
        this.blueprint = "minecraft:diamond";
    }};
    public static ResourceLocation getBlueprint(String creativeModeTab){
        if(creativeModeTab.equals("yuushya_template")) return new ResourceLocation(MOD_ID,"pictor");
        String blueprint = ConfigReader.YuushyaRawItemGroupMap.getOrDefault(creativeModeTab,defaultItemGroup).blueprint;
        if(blueprint==null) return new ResourceLocation("minecraft","diamond");
        if(blueprint.contains(":")) return ResourceLocation.parse(blueprint);
        return new ResourceLocation(MOD_ID,blueprint);
//        return switch (creativeModeTab) {
//            case "yuushya_extrablocks" -> new ResourceLocation(MOD_ID,"block_blueprint");
//            case "yuushya_wood" -> new ResourceLocation(MOD_ID,"wood_blueprint");
//            case "yuushya_stone" -> new ResourceLocation(MOD_ID,"stone_blueprint");
//            case "yuushya_fabric" -> new ResourceLocation(MOD_ID,"fabric_blueprint");
//            case "yuushya_furniture" -> new ResourceLocation(MOD_ID,"furniture_blueprint");
//            case "yuushya_lighting" -> new ResourceLocation(MOD_ID,"lighting_blueprint");
//            case "yuushya_electrical_appliance" -> new ResourceLocation(MOD_ID,"lighting_blueprint");
//            case "yuushya_catering" -> new ResourceLocation(MOD_ID,"catering_blueprint");
//            case "yuushya_window" -> new ResourceLocation(MOD_ID,"window_blueprint");
//            case "yuushya_store" -> new ResourceLocation(MOD_ID,"store_blueprint");
//            case "yuushya_signs" -> new ResourceLocation(MOD_ID,"sign_blueprint");
//            case "yuushya_living_being" -> new ResourceLocation(MOD_ID,"living_being_blueprint");
//            case "yuushya_extrashapes" -> new ResourceLocation(MOD_ID,"extra_shapes_blueprint");
//            case "yuushya_infrastructure" -> new ResourceLocation(MOD_ID,"facility_blueprint");
//            case "yuushya_traffic" -> new ResourceLocation(MOD_ID,"traffic_blueprint");
//            case "yuushya_template" -> new ResourceLocation(MOD_ID,"pictor");
//            //case "yuushya_structure" -> new ResourceLocation(MOD_ID,"extra_shapes_blueprint");
//            default -> new ResourceLocation(MOD_ID,"extra_shapes_blueprint");
//        };
    }
//    private static final List<JsonElement> tempJsons=new ArrayList<>();
//    private static final Consumer<FinishedRecipe> getJsonElementToList=(finishedRecipe)->{
//        tempJsons.clear();
//        tempJsons.add(finishedRecipe.serializeRecipe());
//    };
//    private static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumer, ItemLike answerItemLike, ItemLike baseItemLike, int i) {
//        SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseItemLike), answerItemLike, i)
//                .unlockedBy(getHasName(baseItemLike), has(baseItemLike))
//                .save(consumer, getConversionRecipeName(answerItemLike, baseItemLike) + "_stonecutting");
//    }
//    public static JsonElement genStoneCutterFromBase(ItemLike answerItemLike, ItemLike baseItemLike , int ansNum){
//        stonecutterResultFromBase(getJsonElementToList,answerItemLike,baseItemLike,ansNum);
//        return tempJsons.get(0);
//    }
//    public static JsonElement genStoneCutterRecipe(String name,String group){
//        ItemLike itemLike= YuushyaRegistries.BLOCKS.get(name);
//        ItemLike blueprint= YuushyaCreativeModeTab.getBlueprint(group);
//        return genStoneCutterFromBase(itemLike,blueprint,4);
//    }
//
//
//
////from recipe provider
//    private static InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike) {
//        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).build());
//    }
//
//    private static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> tagKey) {
//        return inventoryTrigger(ItemPredicate.Builder.item().of(tagKey).build());
//    }
//
//    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate ... itemPredicates) {
//        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicates);
//    }
//
//    private static String getHasName(ItemLike itemLike) {
//        return "has_" + getItemName(itemLike);
//    }
//
//    private static String getItemName(ItemLike itemLike) {
//        return Registry.ITEM.getKey(itemLike.asItem()).getPath();
//    }
//
//    private static String getConversionRecipeName(ItemLike itemLike, ItemLike itemLike2) {
//        return getItemName(itemLike) + "_from_" + getItemName(itemLike2);
//    }
}
