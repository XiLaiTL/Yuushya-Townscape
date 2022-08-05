package com.yuushya.datagen;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.yuushya.registries.YuushyaCreativeModeTab;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RecipeData {
    private static final List<JsonElement> tempJsons=new ArrayList<>();
    private static final Consumer<FinishedRecipe> getJsonElementToList=(finishedRecipe)->{
        tempJsons.clear();
        tempJsons.add(finishedRecipe.serializeRecipe());
    };
    private static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumer, ItemLike answerItemLike, ItemLike baseItemLike, int i) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseItemLike), answerItemLike, i)
                .unlockedBy(getHasName(baseItemLike), has(baseItemLike))
                .save(consumer, getConversionRecipeName(answerItemLike, baseItemLike) + "_stonecutting");
    }
    public static JsonElement genStoneCutterFromBase(ItemLike answerItemLike, ItemLike baseItemLike , int ansNum){
        stonecutterResultFromBase(getJsonElementToList,answerItemLike,baseItemLike,ansNum);
        return tempJsons.get(0);
    }
    public static JsonElement genStoneCutterRecipe(String name,String group){
        ItemLike itemLike= YuushyaRegistries.BLOCKS.get(name).get();
        ItemLike blueprint= YuushyaCreativeModeTab.getBlueprint(group);
        return genStoneCutterFromBase(itemLike,blueprint,4);
    }


//from recipe provider
    private static InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).build());
    }

    private static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> tagKey) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tagKey).build());
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate ... itemPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicates);
    }

    private static String getHasName(ItemLike itemLike) {
        return "has_" + getItemName(itemLike);
    }

    private static String getItemName(ItemLike itemLike) {
        return Registry.ITEM.getKey(itemLike.asItem()).getPath();
    }

    private static String getSimpleRecipeName(ItemLike itemLike) {
        return getItemName(itemLike);
    }

    private static String getConversionRecipeName(ItemLike itemLike, ItemLike itemLike2) {
        return getItemName(itemLike) + "_from_" + getItemName(itemLike2);
    }
}
