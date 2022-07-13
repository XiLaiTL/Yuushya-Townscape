package com.yuushya.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModelData {

    private static final List<JsonElement> tempJsons=new ArrayList<>();
    private static final BiConsumer<ResourceLocation, Supplier<JsonElement>> getJsonElementToList=(resourceLocation,supplier)->{
        tempJsons.clear();
        tempJsons.add(supplier.get());
    };


    public static JsonElement genSimpleFlatItemModel( ResourceLocation texture){
        ModelTemplates.FLAT_ITEM.create(texture, TextureMapping.layer0(texture), getJsonElementToList);
        return tempJsons.get(0);
    }
    public static JsonElement genSimpleCubeBlockModel(ResourceLocation texture){
        ModelTemplates.CUBE_ALL.create(texture,TextureMapping.cube(texture),getJsonElementToList);
        return tempJsons.get(0);
    }



}
