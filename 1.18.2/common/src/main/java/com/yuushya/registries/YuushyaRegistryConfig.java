package com.yuushya.registries;

import com.google.gson.*;
import com.yuushya.Yuushya;
import com.yuushya.utils.GsonTools;
import dev.architectury.platform.Platform;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.yuushya.utils.GsonTools.NormalGSON;

//设置配置文件夹中的注册表的读和写，
//游戏开始时开始读，数据都读取到YuushyaData中，让YuushyaRegistries准备注册
public class YuushyaRegistryConfig {

    private static YuushyaRegistryData YuushyaData;
    public static final Map<String,YuushyaRegistryData.Block> YuushyaRawBlockMap=new LinkedHashMap<>();
    public static final Map<String,YuushyaRegistryData.Item> YuushyaRawItemMap=new LinkedHashMap<>();
    public static final Map<String,YuushyaRegistryData.Particle> YuushyaRawParticleMap=new LinkedHashMap<>();

    public static final InputStream InnerFileInputStream=YuushyaRegistryConfig.class.getResourceAsStream("/data/yuushya/register/inner.json");

    public static void addResultToRawMap(YuushyaRegistryData from){
        if (from.block!=null) from.block.forEach((e)->YuushyaRawBlockMap.put(e.name,e));
        if (from.item!=null) from.item.forEach((e)->YuushyaRawItemMap.put(e.name,e));
        if (from.particle!=null) from.particle.forEach((e)->YuushyaRawParticleMap.put(e.name,e));
    }
    public static final File CONFIG_FILE= Platform.getConfigFolder().resolve("com.yuushya/register.json").toFile();
    public static final String VERSION= Platform.getMod(Yuushya.MOD_ID).getVersion();

    public static void readRegistrySelf(){
        readRegistryInner();
        addResultToRawMap(YuushyaData);
    }
    public static void readRegistryConfig() {
        if (!CONFIG_FILE.exists()) {
            readRegistryInner();
            addResultToRawMap(YuushyaData);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            JsonElement configJson = JsonParser.parseReader(reader);
            mergeYuushyaRegistryBlockJson(configJson.getAsJsonObject().getAsJsonArray("block"));
            YuushyaRegistryData yuushyaRegistryData = NormalGSON.fromJson(configJson, YuushyaRegistryData.class);
            if (yuushyaRegistryData.version.equals(VERSION)) {
                addResultToRawMap(yuushyaRegistryData);
            } else {
                readRegistryInner();//如果不是同一个版本的话，优先读取Jar包内部内容
                //使用map来合并两者的数据
                addResultToRawMap(yuushyaRegistryData);
                addResultToRawMap(YuushyaData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readRegistryInner(){
        if (InnerFileInputStream!=null){
            try (BufferedReader reader=new BufferedReader(new InputStreamReader(InnerFileInputStream))){
                JsonElement innerJson=JsonParser.parseReader(reader);
                mergeYuushyaRegistryBlockJson(innerJson.getAsJsonObject().getAsJsonArray("block"));
                YuushyaData=NormalGSON.fromJson(innerJson,YuushyaRegistryData.class);
            }catch (IOException e){e.printStackTrace();}
        }
    }

    //方块A:{classType:B,xxx}，方块B:{classType:class,xxx}，把B的属性合并到A中去
    private static final Map<String,JsonObject> BlockClass=new HashMap<>();
    public static void mergeYuushyaRegistryBlockJson(JsonArray blockList){
        blockList.forEach((block)->{
            JsonObject blockObject=block.getAsJsonObject();
            String classType=blockObject.get("class_type").getAsString();
            if ("class".equals(classType))
                BlockClass.put(blockObject.get("name").getAsString(), blockObject);
            else if (BlockClass.containsKey(classType))
                try {
                    GsonTools.extendJsonObject(blockObject, GsonTools.ConflictStrategy.PREFER_FIRST_OBJ, BlockClass.get(classType));
                } catch (GsonTools.JsonObjectExtensionConflictException e) {e.printStackTrace();}
        });
    }
}
