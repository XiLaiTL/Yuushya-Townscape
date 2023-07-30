package com.yuushya.datagen.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuushya.datagen.utils.ResourceLocation;
import org.gradle.internal.impldep.com.google.api.client.json.Json;

public class LootTableData {
//    private static final Gson GSON = Deserializers.createLootTableSerializer().create();

    //    public static JsonElement genSingleItemTable(ItemLike itemLike) {
//        LootTable.Builder builder=LootTable.lootTable().withPool(
//                LootPool.lootPool()
//                        .setRolls(ConstantValue.exactly(1.0F))
//                        .add(LootItem.lootTableItem(itemLike))
//                        .when(ExplosionCondition.survivesExplosion())
//        );
//        return GSON.toJsonTree(builder.build());
//    }
    public static JsonElement genSingleItemTable(ResourceLocation resourceLocation) {

        return JsonParser.parseString("""
                {"pools":[{"rolls":1.0,"bonus_rolls":0.0,"entries":[{"type":"minecraft:item","name":"%s"}],"conditions":[{"condition":"minecraft:survives_explosion"}]}]}
                """.formatted(resourceLocation.toString()));
    }

    public static JsonElement genTriBlockLootTable(ResourceLocation resourceLocation) {
        return JsonParser.parseString( """
                {
                  "type": "minecraft:block",
                  "pools": [
                    {
                      "rolls": 1,
                      "entries": [
                        {
                          "type": "minecraft:item",
                          "conditions": [
                            {
                              "condition": "minecraft:block_state_property",
                              "block": "%s",
                              "properties": {
                                "pos": "bottom"
                              }
                            }
                          ],
                          "name": "%s"
                        }
                      ],
                      "conditions": [
                        {
                          "condition": "minecraft:survives_explosion"
                        }
                      ]
                    }
                  ]
                 }
                """.formatted(resourceLocation.toString(),resourceLocation.toString()));
    }
}
