package com.yuushya.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuushya.datagen.data.BlockStateData;
import com.yuushya.datagen.data.ModelData;
import com.yuushya.datagen.data.ParticleData;
import com.yuushya.datagen.utils.ResourceLocation;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.ui.YuushyaLog;
import com.yuushya.utils.GsonTools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

import static com.yuushya.utils.GsonTools.NormalGSON;
import static com.yuushya.utils.GsonTools.combineYuushyaDataBlockJson;
import static com.yuushya.datagen.YuushyaDataProvider.DataType;

public class ConfigReader {
    public static final Map<String,String> TemplateBrother = new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Block> StaticBlockALL=new HashMap<>();
    public final Map<String,YuushyaRegistryData.Block> BlockALL=new HashMap<>();
    public final Map<String,YuushyaRegistryData.Block> BlockTemplate=new HashMap<>();
    public final Map<String,YuushyaRegistryData.Block> BlockRemain=new HashMap<>();
    public final Map<String,YuushyaRegistryData.Block> TextureTypeMap=new HashMap<>();
    public final Map<String,YuushyaRegistryData.Block> YuushyaRawBlockMap=new LinkedHashMap<>();
    public final Map<String,YuushyaRegistryData.Item> YuushyaRawItemMap=new LinkedHashMap<>();
    public final Map<String,YuushyaRegistryData.Particle> YuushyaRawParticleMap=new LinkedHashMap<>();
    public static final Map<String,YuushyaRegistryData.ItemGroup> YuushyaRawItemGroupMap = new LinkedHashMap<>();

    private YuushyaRegistryData YuushyaData;

    public static final Path CONFIG_FILE_PATH = Path.of("../config/com.yuushya/register.json");
    public void generateRegistries(String version){

        for (YuushyaRegistryData.Block block:YuushyaRawBlockMap.values()){
            switch (block.classType){
                case "_comment","class"->{}
                case "remain"->{BlockRemain.put(block.name, block);}
                case "template"->{BlockTemplate.put(block.name, block);}
                //case "block"->{BlockOnly.put(block.name,block);}
                default -> {
                    BlockALL.put(block.name, block);
                    StaticBlockALL.put(block.name,block);
                    if (block.blockstate!=null &&block.blockstate.kit!=null&&block.blockstate.kit.equals("block")){
                        if (block.texture==null){ block.texture=new YuushyaRegistryData.Block.Texture();}
                        if (block.texture.type==null){block.texture.type="all";}
                        YuushyaDataProvider.of(block.name).version(version).type(DataType.BlockModel).add(block);}
                    if (block.texture!=null&&block.texture.type!=null&&!block.texture.type.isEmpty()){
                        TextureTypeMap.put(block.name, block);}
                    if(block.autoGenerated!=null) {
                        YuushyaDataProvider dataProvider=YuushyaDataProvider.of(block.name).version(version);
                        if (block.autoGenerated.blockstate) {
                            dataProvider.type(DataType.BlockState).add(block);}
                        if (block.autoGenerated.lootTable) {
                            dataProvider.type(DataType.LootTable).add(block);}
                        if (block.autoGenerated.itemModel) {
                            dataProvider.type(DataType.ItemModel).add(block);}
                        if (block.autoGenerated.recipe){
                            dataProvider.type(DataType.Recipe).add(block);}
                        if(block.autoGenerated.tags!=null&&!block.autoGenerated.tags.isEmpty()){
                            dataProvider.type(DataType.Tags).add(block);}
                    }
                }
            }
        }
        for (YuushyaRegistryData.Block blockRemain:BlockRemain.values()){
            ResourceLocation blockResourceLocation =ResourceLocation.parse(blockRemain.name);
            if (blockRemain.texture==null){blockRemain.texture=new YuushyaRegistryData.Block.Texture();blockRemain.texture.type="all";}
            if (blockRemain.texture.value==null||blockRemain.texture.value.isEmpty()) {
                blockRemain.texture.value= ResourceLocation.parse(blockResourceLocation.getNamespace(),"block/"+blockResourceLocation.getPath()).toString();}
            if (blockRemain.texture.type!=null&&!blockRemain.texture.type.isEmpty()){
                TextureTypeMap.put(blockRemain.name, blockRemain);}
        }

        for (YuushyaRegistryData.Block templateBlock:BlockTemplate.values()){
            if(templateBlock.itemGroup==null) templateBlock.itemGroup = "yuushya_template";//TODO: delete
            if (templateBlock.autoGenerated!=null) {
                if(templateBlock.autoGenerated.itemModel)
                    YuushyaDataProvider.of(templateBlock.name).version(version).type(DataType.ItemModel).add(templateBlock).save();
                if(templateBlock.autoGenerated.recipe)
                    YuushyaDataProvider.of(templateBlock.name).version(version).addTemplateBlockRecipe(templateBlock).save();
            }
            List<String> templateModels=new ArrayList<>();
            if(templateBlock.itemModel!=null) templateModels.add(templateBlock.itemModel);
            if(templateBlock.blockstate.models!=null) templateModels.addAll(BlockStateData.getModelListFromData(templateBlock.blockstate.models)) ;
            if(templateBlock.blockstate.forms!=null) templateBlock.blockstate.forms.forEach((form)->{templateModels.addAll(BlockStateData.getModelListFromData(form));});
            if (templateBlock.texture==null){
                templateBlock.texture=new YuushyaRegistryData.Block.Texture();
                templateModels.forEach(name-> ModelData.setModelTemplate(name,List.of()));}
            else if(templateBlock.texture.slots!=null) templateModels.forEach(name->ModelData.setModelTemplate(name,templateBlock.texture.slots));

            JsonObject templateBlockJson= NormalGSON.toJsonTree(templateBlock,YuushyaRegistryData.Block.class).getAsJsonObject();
            List<YuushyaRegistryData.Block> list=getTemplateUsageList(templateBlock);
            String templateBrother = ResourceLocation.parse(list.get(0).name).getPath();
            for(YuushyaRegistryData.Block block:list){
                JsonObject blockJson= NormalGSON.toJsonTree(block,YuushyaRegistryData.Block.class).getAsJsonObject();
                YuushyaRegistryData.Block blockNew=combineYuushyaDataBlockJson(blockJson,templateBlockJson);
                blockNew.properties.parent=block.name;
                String name;
                if (block.classType.equals("remain")){
                    ResourceLocation blockResourceLocation =ResourceLocation.parse(block.name);
                    name=blockResourceLocation.getPath();
                }
                else{ name= block.name; }
                generateTemplateProduct(name,templateBlock,templateModels,blockNew,templateBrother,version);
            }
        }
        //TODP: add particle to normal block
        for(YuushyaRegistryData.Particle particle: YuushyaRawParticleMap.values()){
            if (particle.spawner==null) particle.spawner=new YuushyaRegistryData.Block();
            if (particle.spawner.properties==null) {particle.spawner.properties=new YuushyaRegistryData.Block.Properties();}
            if (particle.spawner.name==null||particle.spawner.name.isEmpty()) particle.spawner.name=particle.name+"_spawner";

            YuushyaDataProvider dataProvider= YuushyaDataProvider.of(particle.spawner.name).version(version);
            //TODO: the model of particle spawner
            dataProvider.type(DataType.BlockState).json(()->BlockStateData.genSimpleBlock(ResourceLocation.parse("yuushya:extra_building_material/blank"))).save();
            dataProvider.type(DataType.ItemModel).add(particle.spawner);
            dataProvider.type(DataType.LootTable).add(particle.spawner);
            dataProvider.type(DataType.Recipe).add(particle.spawner);
            YuushyaDataProvider.of(particle.name).version(version).type(DataType.Particle).json(()-> ParticleData.genParticleDescription(particle.textures)).save();
        }
    }
    private void generateTemplateProduct(String name, YuushyaRegistryData.Block templateBlock, List<String> templateModels, YuushyaRegistryData.Block block,String templateBrother,String version){
        if(templateBlock.itemModel!=null) block.itemModel = templateBlock.itemModel +"_"+name;
        else block.itemModel = null;
        if(templateBlock.blockstate.models!=null)  block.blockstate.models=templateBlock.blockstate.models.stream().map((s)->s+"_"+name).toList();
        if(templateBlock.blockstate.forms!=null) block.blockstate.forms=templateBlock.blockstate.forms.stream().map((list)->list.stream().map((s)->s+"_"+name).toList()).toList();
        block.name=templateBlock.name+"_"+name;
        YuushyaDataProvider dataProvider= YuushyaDataProvider.of(block.name).version(version);
        YuushyaDataProvider modelDataProvider= YuushyaDataProvider.of(DataType.BlockModel).version(version);
        templateModels.forEach((s)->{
            modelDataProvider.id(ResourceLocation.parse(s+"_"+name)).setPrefix("models/").json(()->ModelData.genTemplateModel(s,templateBlock.texture.slots,block.texture.value,block.texture.set)).save();
        });
        dataProvider.type(DataType.BlockState).add(block).save();
        dataProvider.type(DataType.LootTable).add(block).save();
        dataProvider.type(DataType.ItemModel).add(block).save();
        dataProvider.addTemplateRecipe(block,templateBlock).save();
        if(block.autoGenerated.tags!=null&&!block.autoGenerated.tags.isEmpty()){
            dataProvider.type(DataType.Tags).add(block);}
        BlockALL.put(block.name,block);
        StaticBlockALL.put(block.name,block);
        TemplateBrother.put(block.name,templateBlock.name+"_"+templateBrother);
    }

    public List<YuushyaRegistryData.Block> getTemplateUsageList(YuushyaRegistryData.Block templateBlock){
        List<YuushyaRegistryData.Block> list=new ArrayList<>();
        if (templateBlock.texture==null||(templateBlock.texture.forClass==null&&templateBlock.texture.forSpecified==null) ){
            list.addAll(TextureTypeMap.values());}
        else{
            if (templateBlock.texture.forClass!=null){
                list.addAll(TextureTypeMap.values().stream().filter((e)->templateBlock.texture.forClass.contains(e.texture.type)).toList());}
            if (templateBlock.texture.forSpecified!=null){
                templateBlock.texture.forSpecified.forEach((name)->{
                    ResourceLocation resourceLocation= ResourceLocation.parse(name);
                    if (TextureTypeMap.containsKey(resourceLocation.toString())) list.add(TextureTypeMap.get(resourceLocation.toString()));
                    if (TextureTypeMap.containsKey(resourceLocation.getPath())) list.add(TextureTypeMap.get(resourceLocation.getPath()));
                });
            }
        }
        return list;
    }

    public void readRegistryInner(){
        InputStream InnerFileInputStream= ConfigReader.class.getResourceAsStream("/data/yuushya/register/inner.json");
        if (InnerFileInputStream!=null){
            try (BufferedReader reader=new BufferedReader(new InputStreamReader(InnerFileInputStream))){
                JsonElement innerJson=JsonParser.parseReader(reader);
                mergeYuushyaRegistryBlockClass(innerJson);
                YuushyaData=NormalGSON.fromJson(innerJson,YuushyaRegistryData.class);
            }catch (IOException e){e.printStackTrace();YuushyaLog.error(e);}
        }
    }

    public void readRegistryConfig(Path registerPath){
        //readRegistryInner();
        var files = registerPath.toFile().listFiles(
                x->x.isFile()&&x.toPath().toString().endsWith(".json")
        );
        List<File> list = files==null?new ArrayList<>() :  new ArrayList<>(List.of(files));
        list.add(CONFIG_FILE_PATH.toFile());
        list.sort(Comparator.comparing(File::getPath));
        for(var file:list){
            if(file.exists()) {
                try(BufferedReader reader=new BufferedReader( new FileReader(file))){
                    JsonElement configJson= JsonParser.parseReader(reader);
                    mergeYuushyaRegistryBlockClass(configJson);
                    YuushyaRegistryData yuushyaRegistryData=NormalGSON.fromJson(configJson,YuushyaRegistryData.class);
                    addResultToRawMap(yuushyaRegistryData);
                }catch (IOException e){e.printStackTrace();YuushyaLog.error(e);}
            }
        }
        addResultToRawMap(YuushyaData);
    }
    private void addResultToRawMap(YuushyaRegistryData from){
        if(from!=null){
            if (from.block!=null) from.block.forEach((e)->YuushyaRawBlockMap.put(e.name,e));
            if (from.item!=null) from.item.forEach((e)->YuushyaRawItemMap.put(e.name,e));
            if (from.particle!=null) from.particle.forEach((e)->YuushyaRawParticleMap.put(e.name,e));
            if (from.itemGroup!= null) from.itemGroup.forEach((e)->YuushyaRawItemGroupMap.put(e.name,e));
        }
    }
    private static final Map<String,JsonObject> BlockClass=new HashMap<>();
    public static void mergeYuushyaRegistryBlockClass(JsonElement innerJson){
        if(innerJson instanceof JsonObject jsonObject
                && jsonObject.has("block")
                && jsonObject.get("block") instanceof JsonArray blockArray){
            mergeYuushyaRegistryBlockJson(blockArray);
        }
    }
    public static void mergeYuushyaRegistryBlockJson(JsonArray blockList){
        if(blockList!=null)
            blockList.forEach((block)->{
                JsonObject blockObject=block.getAsJsonObject();
                String classType=blockObject.get("class_type").getAsString();
                if ("class".equals(classType))
                    BlockClass.put(blockObject.get("name").getAsString(), blockObject);
                else if (BlockClass.containsKey(classType))
                    try {
                        GsonTools.extendJsonObject(blockObject, GsonTools.ConflictStrategy.PREFER_FIRST_OBJ, BlockClass.get(classType));
                    } catch (GsonTools.JsonObjectExtensionConflictException e) {e.printStackTrace();YuushyaLog.error(e);  }
            });
    }

    private YuushyaRegistryData getYuushyaRegistryData(){
        JsonArray block=new JsonArray();YuushyaRawBlockMap.values().forEach(e->block.add(NormalGSON.toJsonTree(e)));
        JsonArray item=new JsonArray();YuushyaRawItemMap.values().forEach(e->item.add(NormalGSON.toJsonTree(e)));
        JsonArray particle=new JsonArray();YuushyaRawParticleMap.values().forEach(e->particle.add(NormalGSON.toJsonTree(e)));
        mergeYuushyaRegistryBlockJson(block);
        JsonObject json=new JsonObject();
        json.add("block",block);
        json.add("item",item);
        json.add("particle",particle);
        YuushyaRegistryData yuushyaRegistryData=NormalGSON.fromJson(json,YuushyaRegistryData.class);
        //yuushyaRegistryData.version= VERSION;
        return yuushyaRegistryData;
    }
    public void writeRegistryConfig(){
        try{
            CONFIG_FILE_PATH.getParent().toFile().mkdirs();
            CONFIG_FILE_PATH.toFile().createNewFile();
            YuushyaRegistryData yuushyaRegistryData= getYuushyaRegistryData();
            String json= NormalGSON.toJson(yuushyaRegistryData);
            try(BufferedWriter writer=new BufferedWriter(new FileWriter(CONFIG_FILE_PATH.toFile(),StandardCharsets.UTF_8))){
                writer.write(json);
            }catch (IOException e){e.printStackTrace();  YuushyaLog.error(e);}
        }catch (IOException e){e.printStackTrace(); YuushyaLog.error(e);}
    }
}
