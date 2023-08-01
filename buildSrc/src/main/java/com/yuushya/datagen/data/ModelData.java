package com.yuushya.datagen.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuushya.datagen.utils.ModelTemplate;
import com.yuushya.datagen.utils.ResourceLocation;
import com.yuushya.datagen.utils.TextureMapping;
import com.yuushya.datagen.utils.TextureSlot;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.yuushya.datagen.data.BlockStateData.STATE_SPLITTER;

public class ModelData {

    private static final List<JsonElement> tempJsons=new ArrayList<>();
    private static final BiConsumer<ResourceLocation, Supplier<JsonElement>> getJsonElementToList=(resourceLocation,supplier)->{
        tempJsons.clear();
        tempJsons.add(supplier.get());
    };

    public static JsonElement genChildItemModel(ResourceLocation parent){
        if (parent==null) parent=ResourceLocation.parse("block/stone");
        JsonObject json =new JsonObject();
        json.addProperty("parent",parent.toString());
        return json;
    }
    public static JsonElement genSimpleFlatItemModel( ResourceLocation texture){
        ModelTemplate.FLAT_ITEM.create(texture, TextureMapping.layer0(texture), getJsonElementToList);
        return tempJsons.get(0);
    }
    public static JsonElement genSimpleCubeBlockModel(ResourceLocation texture){
        ModelTemplate.CUBE_ALL.create(texture, TextureMapping.cube(texture),getJsonElementToList);
        return tempJsons.get(0);
    }

    private static final Map<String,ModelTemplate> YuushyaModelTemplateMap =new HashMap<>();
    private static ModelTemplate createModelTemplate(ResourceLocation resourceLocation, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(resourceLocation), Optional.empty(), textureSlots);
    }

    private static final Map<String, TextureSlot> textureSlotMap=new HashMap<>();
    public static TextureSlot getTextureSlot(String textureSlotString){
        if (textureSlotMap.containsKey(textureSlotString)) return textureSlotMap.get(textureSlotString);
        textureSlotMap.put(textureSlotString,new TextureSlot(textureSlotString,TextureSlot.ALL));
        return textureSlotMap.get(textureSlotString);
    }
    public static void setModelTemplate(String name,List<String> textureSlotString) {//name= "namespace:models/block/xxx"
        if (textureSlotString==null||textureSlotString.isEmpty()){
            YuushyaModelTemplateMap.put(name,createModelTemplate(ResourceLocation.parse(name),TextureSlot.ALL));}
        else{
            List<TextureSlot> textureSlots=textureSlotString.stream().map(ModelData::getTextureSlot).toList();
            YuushyaModelTemplateMap.put(name,createModelTemplate(ResourceLocation.parse(name),textureSlots.toArray(TextureSlot[]::new)));}
    }
    public static TextureMapping getTextureMapping(List<String> slots,List<String> textures){
        TextureMapping textureMapping=new TextureMapping();
        textures.forEach(e->{
            Iterator<String> iterator = STATE_SPLITTER.split(e).iterator();
            String slot = iterator.next();
            String texture = iterator.next();
            if (slots.contains(slot)){
                TextureSlot textureSlot = slot.equals("all")
                        ?TextureSlot.ALL
                        :getTextureSlot(slot);
                ResourceLocation resourceLocation=ResourceLocation.parse(texture);
                if (textureSlot!=null){
                    textureMapping.put(textureSlot,resourceLocation);
                }
            }
        });
        return textureMapping;
    }
    public static JsonElement genTemplateModel(String templateName,List<String> slots,String singleTexture,List<String> textures){
        ResourceLocation noUse=ResourceLocation.parse(singleTexture);
        if ((slots==null||slots.isEmpty())&&(textures==null|| textures.isEmpty()))
            YuushyaModelTemplateMap.get(templateName).create(noUse,TextureMapping.cube(noUse),getJsonElementToList);
        else if((slots==null||slots.isEmpty()))
            YuushyaModelTemplateMap.get(templateName).create(noUse,getTextureMapping(List.of("all"),textures),getJsonElementToList);
        else{
            YuushyaModelTemplateMap.get(templateName).create(noUse,getTextureMapping(slots,textures),getJsonElementToList);}
        return tempJsons.get(0);
    }
}
