package com.yuushya.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.yuushya.datagen.BlockStateData.STATE_SPLITTER;

public class ModelData {

    private static final List<JsonElement> tempJsons=new ArrayList<>();
    private static final BiConsumer<ResourceLocation, Supplier<JsonElement>> getJsonElementToList=(resourceLocation,supplier)->{
        tempJsons.clear();
        tempJsons.add(supplier.get());
    };

    public static JsonElement genChildItemModel(ResourceLocation parent){
        JsonObject json =new JsonObject();json.addProperty("parent",parent.toString());
        return json;
    }
    public static JsonElement genSimpleFlatItemModel( ResourceLocation texture){
        ModelTemplates.FLAT_ITEM.create(texture, TextureMapping.layer0(texture), getJsonElementToList);
        return tempJsons.get(0);
    }
    public static JsonElement genSimpleCubeBlockModel(ResourceLocation texture){
        ModelTemplates.CUBE_ALL.create(texture,TextureMapping.cube(texture),getJsonElementToList);
        return tempJsons.get(0);
    }

    private static final Map<String,ModelTemplate> YuushyaModelTemplateMap =new HashMap<>();
    private static ModelTemplate createModelTemplate(ResourceLocation resourceLocation, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(resourceLocation), Optional.empty(), textureSlots);
    }

    public static TextureSlot getTextureSlot(String textureSlotString){
        try{
            return TextureSlot.class.getDeclaredConstructor(String.class,TextureSlot.class).newInstance(textureSlotString,TextureSlot.ALL);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException invocationTargetException) {invocationTargetException.printStackTrace();}
        return null;
    }
    public static void setModelTemplate(String name,List<String> textureSlotString) {//name= "namespace:models/block/xxx"
        if (textureSlotString==null||textureSlotString.isEmpty()){
            YuushyaModelTemplateMap.put(name,createModelTemplate(new ResourceLocation(name),TextureSlot.ALL));}
        else{
            List<TextureSlot> textureSlots=textureSlotString.stream().map(ModelData::getTextureSlot).toList();
            YuushyaModelTemplateMap.put(name,createModelTemplate(new ResourceLocation(name),textureSlots.toArray(TextureSlot[]::new)));}
    }
    public static TextureMapping getTextureMapping(List<String> slots,List<String> textures){
        TextureMapping textureMapping=new TextureMapping();
        textures.forEach(e->{
            Iterator<String> iterator = STATE_SPLITTER.split(e).iterator();
            String slot = iterator.next();
            String texture = iterator.next();
            if (slots.contains(slot)){
                TextureSlot textureSlot=getTextureSlot(slot);
                ResourceLocation resourceLocation=ResourceLocation.tryParse(texture);
                if (textureSlot!=null&&resourceLocation!=null){
                    textureMapping.put(textureSlot,resourceLocation);
                }
            }
        });
        return textureMapping;
    }
    public static JsonElement genTemplateModel(String templateName,List<String> slots,String singleTexture,List<String> textures){
        ResourceLocation noUse=new ResourceLocation(singleTexture);
        if ((slots==null||slots.isEmpty())&&(textures==null|| textures.isEmpty()))
            return genTemplateModel(templateName,noUse);
        YuushyaModelTemplateMap.get(templateName).create(noUse,getTextureMapping(slots,textures),getJsonElementToList);
        return tempJsons.get(0);
    }
    public static JsonElement genTemplateModel(String templateName,ResourceLocation texture){
        YuushyaModelTemplateMap.get(templateName).create(texture,TextureMapping.cube(texture),getJsonElementToList);
        return tempJsons.get(0);
    }


}
