package com.yuushya.datagen.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yuushya.datagen.utils.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagsData {
    private static final Map<ResourceLocation, List<String>> tagsCache = new HashMap<>();
    public enum Type{
        BLOCKS("blocks"),ENTITY_TYPES("entity_types"),FLUIDS("fluids"),FUNCTIONS("functions"),GAME_EVENTS("game_events"),ITEMS("items"),BIOME("worldgen/biome"),CONFIGURED_STRUCTURE_FEATURE("worldgen/configured_structure_feature");
        public final String type;
        Type(String type){this.type = type;}
        public static Type toType(String type){
            if(type==null) return BLOCKS;
            return switch (type){
                case "blocks","block"->BLOCKS;
                case "entity_types","entity"->ENTITY_TYPES;
                case "fluids","fluid"->FLUIDS;
                case "functions","function"->FUNCTIONS;
                case "game_events","event"->GAME_EVENTS;
                case "items","item"->ITEMS;
                case "biome"->BIOME;
                case "configured_structure_feature"->CONFIGURED_STRUCTURE_FEATURE;
                default -> BLOCKS;
            };
        }
    }
    public static ResourceLocation addTag(String type, ResourceLocation tagName, ResourceLocation id){
        ResourceLocation key = new ResourceLocation(tagName.getNamespace(),  Type.toType(type).type+"/"+tagName.getPath() );
        tagsCache.putIfAbsent(key,new ArrayList<>());
        tagsCache.get(key).add(id.toString());
        System.out.println(tagsCache.get(key).size());
        return key;
    }

    //这里的输入是addTag的输出
    public static JsonElement genTags(ResourceLocation dataKey){
        JsonArray array = new JsonArray();
        tagsCache.get(dataKey).forEach(array::add);

        JsonObject json = new JsonObject();
        json.add("replace", new JsonPrimitive(false));
        json.add("values", array);
        return json;
    }
}
