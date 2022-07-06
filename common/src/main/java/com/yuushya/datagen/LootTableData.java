package com.yuushya.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.yuushya.Yuushya;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableData {
    private static final Gson GSON = Deserializers.createLootTableSerializer().create();

    private static LootTable.Builder createSingleItemTable(ItemLike itemLike) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(itemLike))
                        .when(ExplosionCondition.survivesExplosion())
        );
    }

    public static JsonElement genSingleItemTable(ItemLike itemLike) {
        return GSON.toJsonTree(createSingleItemTable(itemLike).build());
    }

}
