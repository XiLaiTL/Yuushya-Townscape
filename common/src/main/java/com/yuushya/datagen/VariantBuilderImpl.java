package com.yuushya.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;

public class VariantBuilderImpl implements ModelBuilder {

    private ResourceLocation identifier;
    private String variants;

    //省略setter方法
    public VariantBuilderImpl setIdentifier(ResourceLocation identifier){
        this.identifier=identifier;
        return this;
    }
    public VariantBuilderImpl setVariants(String variants){
        this.variants=variants;
        return this;
    }

    private void save(){
        //为了在加载是和mc的identifier对比，因此这里要在path加上前后缀，同时保存进map，便于加载时调用
        BuilderPool.variantBuilderPool.put(new ResourceLocation(this.identifier.getNamespace(), "blockstates/" + this.identifier.getPath()).toString() + ".json", this);
    }


    public String genJson() {
        // You can genJson only after it has been properly initialized
        if (identifier == null) return "";
        JsonObject json = new JsonObject();
        JsonObject typeInfoJsonObject;
        JsonObject typeJsonObject;
        //当variants为空时，默认只有一个状态
        if (variants == null || variants.isEmpty()){
            typeInfoJsonObject = new JsonObject();
            typeInfoJsonObject.addProperty("model", identifier.getNamespace() +":item/"+ identifier.getPath());
            typeJsonObject = new JsonObject();
            typeJsonObject.add("", typeInfoJsonObject);
            json.add("variants", typeJsonObject);
            return json.toString();
        }
        json.add("variants", new JsonParser().parse(variants).getAsJsonObject());
        return json.toString();
    }

    public void register() {
        //无需注册进mc，只用保存进map即可
        save();
    }

}