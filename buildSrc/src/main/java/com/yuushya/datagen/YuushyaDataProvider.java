package com.yuushya.datagen;

import com.google.gson.JsonElement;
import com.yuushya.datagen.data.BlockStateData;
import com.yuushya.datagen.data.LootTableData;
import com.yuushya.datagen.data.ModelData;
import com.yuushya.datagen.data.RecipeData;
import com.yuushya.datagen.utils.ResourceLocation;
import com.yuushya.registries.YuushyaRegistryData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.yuushya.datagen.data.BlockStateData.getModelListFromData;
import static com.yuushya.datagen.data.RecipeData.getBlueprint;
import static com.yuushya.datagen.data.RecipeData.getResultNumber;
import static com.yuushya.datagen.utils.Utils.MOD_ID;

public class YuushyaDataProvider {
    public enum ResourceType{
        ASSETS,DATA
    }
    public enum DataType{
        BlockState("blockstates/",ResourceType.ASSETS),
        BlockModel("models/block/",ResourceType.ASSETS),
        ItemModel("models/item/",ResourceType.ASSETS),
        Recipe("recipes/block/",ResourceType.DATA),
        LootTable("loot_tables/blocks/",ResourceType.DATA),
        Particle("particles/",ResourceType.ASSETS);
        private final String prefix;
        public final ResourceType resource;
        public final Map<ResourceLocation, Supplier<JsonElement>> map = new HashMap<>();
        private static final String suffix=".json";
        DataType(String prefix,ResourceType resource){
            this.prefix=prefix; this.resource=resource;
        }
    }

    private DataType dataType;
    private ResourceLocation resourceLocation;
    private Supplier<JsonElement> jsonElementSupplier;
    private String selfPrefix;

    public YuushyaDataProvider id(String name){
        this.resourceLocation=new ResourceLocation(MOD_ID,name);
        return this;
    }
    public YuushyaDataProvider id(ResourceLocation resourceLocation){
        this.resourceLocation=resourceLocation;
        return this;
    }
    public YuushyaDataProvider type(DataType dataType){
        this.dataType=dataType;
        return this;
    }
    public static YuushyaDataProvider of(ResourceLocation resourceLocation){
        return new YuushyaDataProvider().id(resourceLocation);
    }
    public static YuushyaDataProvider of(String name){
        return new YuushyaDataProvider().id(name);
    }
    public static YuushyaDataProvider of(DataType dataType){
        return new YuushyaDataProvider().type(dataType);
    }
    public YuushyaDataProvider json(Supplier<JsonElement>  jsonElementSupplier){
        this.jsonElementSupplier=jsonElementSupplier;
        return this;
    }
    public YuushyaDataProvider setPrefix(String s){
        this.selfPrefix=s;
        return this;
    }
    public ResourceLocation getNewId(){
        return selfPrefix==null||selfPrefix.isEmpty()
                ? new ResourceLocation(this.resourceLocation.getNamespace(),this.dataType.prefix+this.resourceLocation.getPath()+DataType.suffix)
                : new ResourceLocation(this.resourceLocation.getNamespace(),this.selfPrefix+this.resourceLocation.getPath()+DataType.suffix);
    }
    public void save(){
        ResourceLocation resourceLocationNew = getNewId();
        this.dataType.map.put(resourceLocationNew,this.jsonElementSupplier);
    }
    public JsonElement get(ResourceLocation resourceLocation){
        return this.dataType.map.get(resourceLocation).get();
    }
    public JsonElement get(){
        ResourceLocation resourceLocationNew = getNewId();
        return this.dataType.map.get(resourceLocationNew).get();
    }
    public Boolean contain(ResourceLocation resourceLocation){
        return this.dataType.map.containsKey(resourceLocation);
    }
    public Boolean contain(){
        ResourceLocation resourceLocationNew = getNewId();
        return this.dataType.map.containsKey(resourceLocationNew);
    }
    public void forEach(BiConsumer<? super ResourceLocation, ? super Supplier<JsonElement>> action){
        this.dataType.map.forEach(action);
    }

    public static ResourceLocation toRecipeResourceLocation(ResourceLocation resourceLocation){
        return new ResourceLocation(
                resourceLocation.getNamespace(),
                resourceLocation.getPath().replace("recipes/","").replace(".json",""));
    }
    public static ResourceLocation toLootTableResourceLocation(ResourceLocation resourceLocation){
        return new ResourceLocation(
                resourceLocation.getNamespace(),
                resourceLocation.getPath().replace("loot_tables/","").replace(".json",""));
    }

    public YuushyaDataProvider addTemplateBlockRecipe(YuushyaRegistryData.Block templateBlock){
        this.id(templateBlock.name);
        this.type(DataType.Recipe);
        this.json(()-> RecipeData.genStoneCutterRecipe(new ResourceLocation(MOD_ID,templateBlock.name) ,getBlueprint("yuushya_template"),1 )).save();
        return this;
    }
    public YuushyaDataProvider addTemplateRecipe(YuushyaRegistryData.Block block,YuushyaRegistryData.Block templateBlock){
        this.id(block.name);
        this.type(DataType.Recipe);
        this.json(()-> RecipeData.genStoneCutterRecipe(new ResourceLocation(MOD_ID,block.name) ,new ResourceLocation(MOD_ID,templateBlock.name),1 )).save();
        return this;
    }

    public YuushyaDataProvider add(YuushyaRegistryData.Block block){
        this.id(block.name);
        switch (this.dataType){
            case BlockState ->{
                if (block.blockstate==null||(block.blockstate.kit!=null&&block.blockstate.kit.equals("block")))
                    this.json(()-> BlockStateData.genSimpleBlock(new ResourceLocation(MOD_ID,"block/"+block.name))).save();
                else
                    this.json(()->BlockStateData.genBlockState(block.blockstate)).save();
            }
            case ItemModel -> {
                ResourceLocation modelUse;
                if (block.itemModel!=null&&!block.itemModel.isEmpty())
                    modelUse=new ResourceLocation(block.itemModel);
                else if (block.blockstate==null||(block.blockstate.kit!=null&&block.blockstate.kit.equals("block")))
                    modelUse=new ResourceLocation(MOD_ID,"block/"+block.name);
                else if (block.blockstate.kit!=null&&!block.blockstate.kit.isEmpty())
                    modelUse=new ResourceLocation(block.blockstate.forms.get(0).get(0));
                else if (block.blockstate.states!=null&&!block.blockstate.states.isEmpty())
                    modelUse=new ResourceLocation(getModelListFromData(block.blockstate.models).get(0));
                else
                    modelUse=null;
                this.json(()-> ModelData.genChildItemModel(modelUse)).save();
            }
            case Recipe -> this.json(()-> RecipeData.genStoneCutterRecipe(new ResourceLocation(MOD_ID,block.name) ,getBlueprint(block.itemGroup),getResultNumber(block.itemGroup) )).save();
            case BlockModel -> {this.json(()->ModelData.genSimpleCubeBlockModel(new ResourceLocation(block.texture.value))).save();}
            case LootTable -> {
                if(block.blockstate != null && "tri_part".equals(block.blockstate.kit))
                    this.json(() -> LootTableData.genTriBlockLootTable(new ResourceLocation(MOD_ID, block.name))).save();
                else
                    this.json(() -> LootTableData.genSingleItemTable(new ResourceLocation(MOD_ID, block.name))).save();
            }
        };
        return this;
    }

    public void clear(){
        dataType.map.clear();
    }
    public static void clearAll(){
        for (DataType value : DataType.values()) {
            value.map.clear();
        }
    }
}
