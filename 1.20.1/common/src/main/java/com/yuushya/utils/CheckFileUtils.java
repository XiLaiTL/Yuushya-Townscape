package com.yuushya.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.architectury.platform.Platform;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckFileUtils {
    public static final Gson NormalGSON = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    public record Info(
            String id,
            String type,
            List<String> api,
            String version,
            List<String> minecraft,
            Map<String,String> link
    ){ }

    private static Map<String,Info> information = new HashMap<>();
    public static Info info(String id){ return information.get(id); }

    public static final Map<String,Info> ctm = new HashMap<>();
    public static final Map<String,Info> resourcepacks = new HashMap<>();
    public static final Map<String,Info> recommend = new HashMap<>();
    private static String version;
    private static String api;
    private static final InputStream INFORMATION_FILE = CheckFileUtils.class.getResourceAsStream("/assets/yuushya/information/information.json");

    public static void loadInformation(){
        version = SharedConstants.getCurrentVersion().getName();
        api = Platform.isFabric()?"fabric"
                :Platform.isForge()?"forge":"";
        if (INFORMATION_FILE != null) {
            try(BufferedReader bufferedReader =  new BufferedReader(new InputStreamReader(INFORMATION_FILE))){
                JsonElement jsonElement = JsonParser.parseReader(bufferedReader);
                information = NormalGSON.fromJson(jsonElement,new TypeToken<Map<String,Info>>(){}.getType());
                for(Map.Entry<String, Info> entry:information.entrySet()){
                    Info info = entry.getValue();
                    if(info.api.contains(api) &&info.minecraft.contains(version)){
                        switch (info.type){
                            case "ctm"->ctm.put(entry.getKey(),info);
                            case "resourcepack"->resourcepacks.put(entry.getKey(),info);
                            case "recommend"->recommend.put(entry.getKey(),info);
                        }
                    }
                }
            } catch (IOException e) {

            }
        }
    }

    public static void printInformation(){
        for(Info info:information.values()){
            System.out.println(info);
        }
    }

    //如果已经安装CTM，返回empty，没安装返回整个
    public static List<Info> checkCTM(){
        for(Info info: ctm.values()){
            if(checkMod(info.id)) return List.of();
        }
        return ctm.values().stream().toList();
    }
    //返回没有安装的部分！
    public static List<Info> checkResourcePacks(){
        return resourcepacks.values().stream().filter(info->!checkResourceFile(info.id)).toList();
    }

    private static final String MOD_ID_INDIUM = "indium";
    private static final String MOD_ID_SODIUM = "sodium";
    private static final String MOD_ID_YUUSHYA_MODELLING = "yuushya_modelling";
    private static final String MOD_ID_CONTINUITY = "continuity";
    public static List<Info> checkRecommend(){
        List<Info> list = new ArrayList<>(recommend.values().stream().filter(info->!MOD_ID_INDIUM.equals(info.id)||!checkResourceFile(info.id)).toList());
        if( Platform.isFabric() && checkMod(MOD_ID_SODIUM) && (checkMod(MOD_ID_YUUSHYA_MODELLING)||checkMod(MOD_ID_CONTINUITY)) && !checkMod(MOD_ID_INDIUM)){
            list.add(recommend.get(MOD_ID_INDIUM));
        }
        return list;
    }

    public static boolean checkMod(String id){
        return Platform.getModIds().contains(id);
    }

    public static boolean checkResourceFile(String id){
        Minecraft minecraft = Minecraft.getInstance();
        for(var selectedId : minecraft.getResourcePackRepository().getSelectedIds()){
            if(selectedId.toLowerCase().contains(id)) return true;
        }
        return false;
    }

    public static int compareVersion(String version1,String version2){
        String ver1 = version1.substring(2);
        String ver2 = version2.substring(2);
        return Double.compare(Double.parseDouble(ver1),Double.parseDouble(ver2));
    }

    /**
     * [begin,end)
     */
    public static boolean between(String version, String begin, String end){
        if(begin==null){
            if(end == null) return true;
            return compareVersion(version,end)<0;
        }
        if(end==null) return compareVersion(version,begin)>=0;
        return compareVersion(version,begin)>=0 && compareVersion(version,end)<0;
    }

}
