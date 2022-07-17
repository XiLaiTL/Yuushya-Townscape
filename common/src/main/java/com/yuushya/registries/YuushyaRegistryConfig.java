package com.yuushya.registries;

import com.google.gson.*;
import com.yuushya.Yuushya;
import com.yuushya.utils.GsonTools;
import com.yuushya.utils.YuushyaLogger;
import com.yuushya.utils.YuushyaUtils;
import dev.architectury.platform.Platform;
import net.minecraft.util.GsonHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//设置配置文件夹中的注册表的读和写，
//游戏开始时开始读，数据都读取到YuushyaData中，让YuushyaRegistries准备注册
public class YuushyaRegistryConfig {
    public static YuushyaRegistryData YuushyaData;
    public static final File CONFIG_FILE= Platform.getConfigFolder().resolve("net.cocofish.yuushya/register.json").toFile();
    public static final String VERSION=Platform.getMod(Yuushya.MOD_ID).getVersion();
    public static final InputStream InnerFileInputStream=YuushyaRegistryConfig.class.getResourceAsStream("/assets/yuushya/register/inner.json");
    public static void writeRegistryConfig(){
        try{
            CONFIG_FILE.getParentFile().mkdirs();
            CONFIG_FILE.createNewFile();
            YuushyaRegistryData yuushyaRegistryData=YuushyaResourceReloadListener.getYuushyaRegistryData();
            String json= YuushyaUtils.NormalGSON.toJson(yuushyaRegistryData);
            try(BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8))){
                writer.write(json);
            }catch (IOException e){e.printStackTrace();}
        }catch (IOException e){e.printStackTrace();}
    }
    public static void readRegistryConfig(){
        if (!CONFIG_FILE.exists()){
            readRegistryInner();
            return;
        }
        try(BufferedReader reader=new BufferedReader(new InputStreamReader( new FileInputStream(CONFIG_FILE),StandardCharsets.UTF_8))){
            JsonElement configJson= JsonParser.parseReader(reader);
            mergeYuushyaRegistryBlockJson(configJson.getAsJsonObject().getAsJsonArray("block"));
            YuushyaRegistryData yuushyaRegistryData=YuushyaUtils.NormalGSON.fromJson(configJson,YuushyaRegistryData.class);
            if (yuushyaRegistryData.version.equals(VERSION)){
                YuushyaData=yuushyaRegistryData;
            }
            else {
                readRegistryInner();//如果不是同一个版本的话，优先读取Jar包内部内容
                //使用map来合并两者的数据
                Map<String,YuushyaRegistryData.Block> YuushyaRawBlockMap=new HashMap<>();
                Map<String,YuushyaRegistryData.Item> YuushyaRawItemMap = new HashMap<>();
                Map<String,YuushyaRegistryData.Particle> YuushyaRawParticleMap =new HashMap<>();
                yuushyaRegistryData.block.forEach((e)->YuushyaRawBlockMap.put(e.name,e));
                yuushyaRegistryData.item.forEach((e)->YuushyaRawItemMap.put(e.name,e));
                yuushyaRegistryData.particle.forEach((e)->YuushyaRawParticleMap.put(e.name,e));
                YuushyaData.block.forEach((e)->YuushyaRawBlockMap.put(e.name,e));
                YuushyaData.item.forEach((e)->YuushyaRawItemMap.put(e.name,e));
                YuushyaData.particle.forEach((e)->YuushyaRawParticleMap.put(e.name,e));
                YuushyaData.block=  YuushyaRawBlockMap.values().stream().toList();
                YuushyaData.item=  YuushyaRawItemMap.values().stream().toList();
                YuushyaData.particle= YuushyaRawParticleMap.values().stream().toList();
            }

        }catch (IOException e){e.printStackTrace();}
    }
    public static void readRegistryInner(){
        if (InnerFileInputStream!=null){
            try (BufferedReader reader=new BufferedReader(new InputStreamReader(InnerFileInputStream))){
                JsonElement innerJson=JsonParser.parseReader(reader);
                mergeYuushyaRegistryBlockJson(innerJson.getAsJsonObject().getAsJsonArray("block"));
                YuushyaData=YuushyaUtils.NormalGSON.fromJson(innerJson,YuushyaRegistryData.class);
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
