package com.yuushya.datagen;

import com.google.gson.JsonElement;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class YuushyaDataProvider {
    public enum DataType{
        BlockState("blockstates/"),BlockModel("models/block/"),ItemModel("models/item/"),LootTable("loot_tables/blocks/");
        private final String prefix;
        private static final String suffix=".json";
        DataType(String prefix){
            this.prefix=prefix;
        }
    }

    private static final Map<ResourceLocation, JsonElement> YuushyaBlockStateMap = new HashMap<>();
    private static final Map<ResourceLocation, JsonElement> YuushyaBlockModelMap = new HashMap<>();
    private static final Map<ResourceLocation, JsonElement> YuushyaItemModelMap = new HashMap<>();
    private static final Map<ResourceLocation, JsonElement> YuushyaLootTableMap = new HashMap<>();
    private static final Map<ResourceLocation, VoxelShape> YuushyaVoxelShapeMap = new HashMap<>();

    private DataType dataType;
    private ResourceLocation resourceLocation;
    private JsonElement jsonElement;

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
    public static YuushyaDataProvider of(DataType dataType){
        return new YuushyaDataProvider().type(dataType);
    }
    public YuushyaDataProvider json(JsonElement jsonElement){
        switch (this.dataType){
            case BlockState,BlockModel,ItemModel,LootTable->this.jsonElement=jsonElement;
        }
        return this;
    }
    public ResourceLocation getNewId(){
        return switch (this.dataType){
            case BlockState,BlockModel,ItemModel->new ResourceLocation(this.resourceLocation.getNamespace(),this.dataType.prefix+this.resourceLocation.getNamespace()+DataType.suffix);
            case LootTable -> new ResourceLocation(this.resourceLocation.getNamespace(),this.dataType.prefix+this.resourceLocation.getPath());
            default -> this.resourceLocation;
        };
    }
    public void save(){
        ResourceLocation resourceLocationNew = getNewId();
        switch (this.dataType){
            case BlockState -> YuushyaBlockStateMap.put(resourceLocationNew,this.jsonElement);
            case BlockModel -> YuushyaBlockModelMap.put(resourceLocationNew,this.jsonElement);
            case ItemModel -> YuushyaItemModelMap.put(resourceLocationNew,this.jsonElement);
            case LootTable -> YuushyaLootTableMap.put(resourceLocationNew,this.jsonElement);
        }
    }
    public JsonElement get(ResourceLocation resourceLocation){
        return switch (this.dataType){
            case BlockState -> YuushyaBlockStateMap.get(resourceLocation);
            case BlockModel -> YuushyaBlockModelMap.get(resourceLocation);
            case ItemModel -> YuushyaItemModelMap.get(resourceLocation);
            case LootTable -> YuushyaLootTableMap.get(resourceLocation);
        };
    }
    public JsonElement get(){
        ResourceLocation resourceLocationNew = getNewId();
        return switch (this.dataType){
            case BlockState -> YuushyaBlockStateMap.get(resourceLocationNew);
            case BlockModel -> YuushyaBlockModelMap.get(resourceLocationNew);
            case ItemModel -> YuushyaItemModelMap.get(resourceLocationNew);
            case LootTable -> YuushyaLootTableMap.get(resourceLocationNew);
        };
    }
    public Boolean contain(ResourceLocation resourceLocation){
        return switch (this.dataType){
            case BlockState -> YuushyaBlockStateMap.containsKey(resourceLocation);
            case BlockModel -> YuushyaBlockModelMap.containsKey(resourceLocation);
            case ItemModel -> YuushyaItemModelMap.containsKey(resourceLocation);
            case LootTable -> YuushyaLootTableMap.containsKey(resourceLocation);
        };
    }
    public Boolean contain(){
        ResourceLocation resourceLocationNew = getNewId();
        return switch (this.dataType){
            case BlockState -> YuushyaBlockStateMap.containsKey(resourceLocationNew);
            case BlockModel -> YuushyaBlockModelMap.containsKey(resourceLocationNew);
            case ItemModel -> YuushyaItemModelMap.containsKey(resourceLocationNew);
            case LootTable -> YuushyaLootTableMap.containsKey(resourceLocationNew);
        };
    }
    public void forEach(BiConsumer<? super ResourceLocation, ? super JsonElement> action){
        switch (this.dataType){
            case BlockState -> YuushyaBlockStateMap.forEach(action);
            case BlockModel -> YuushyaBlockModelMap.forEach(action);
            case ItemModel -> YuushyaItemModelMap.forEach(action);
            case LootTable -> YuushyaLootTableMap.forEach(action);
        };
    }

    public static ResourceLocation toLootTableResourceLocation(ResourceLocation resourceLocation){
        return new ResourceLocation(
                resourceLocation.getNamespace(),
                resourceLocation.getPath().replace("loot_tables/","").replace(".json",""));
    }
}