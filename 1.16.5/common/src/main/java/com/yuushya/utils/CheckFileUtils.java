package com.yuushya.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.shedaniel.architectury.platform.Mod;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class CheckFileUtils {
    public static final Gson NormalGSON = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    public final class Info {
        private final String id;
        private final String type;
        private final List<String> api;
        private final String version;
        private final List<String> minecraft;
        private final Map<String, String> link;

        public Info(
                String id,
                String type,
                List<String> api,
                String version,
                List<String> minecraft,
                Map<String, String> link
        ) {
            this.id = id;
            this.type = type;
            this.api = api;
            this.version = version;
            this.minecraft = minecraft;
            this.link = link;
        }

        public String id() {
            return id;
        }

        public String type() {
            return type;
        }

        public List<String> api() {
            return api;
        }

        public String version() {
            return version;
        }

        public List<String> minecraft() {
            return minecraft;
        }

        public Map<String, String> link() {
            return link;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            Info that = (Info) obj;
            return Objects.equals(this.id, that.id) &&
                    Objects.equals(this.type, that.type) &&
                    Objects.equals(this.api, that.api) &&
                    Objects.equals(this.version, that.version) &&
                    Objects.equals(this.minecraft, that.minecraft) &&
                    Objects.equals(this.link, that.link);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, type, api, version, minecraft, link);
        }

        @Override
        public String toString() {
            return "Info[" +
                    "id=" + id + ", " +
                    "type=" + type + ", " +
                    "api=" + api + ", " +
                    "version=" + version + ", " +
                    "minecraft=" + minecraft + ", " +
                    "link=" + link + ']';
        }
    }

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
                JsonElement jsonElement = new JsonParser().parse(bufferedReader);
                information = NormalGSON.fromJson(jsonElement,new TypeToken<Map<String,Info>>(){}.getType());
                for(Map.Entry<String, Info> entry:information.entrySet()){
                    Info info = entry.getValue();
                    if(info.api.contains(api) &&info.minecraft.contains(version)){
                        switch (info.type) {
                            case "ctm":
                                ctm.put(entry.getKey(), info);
                                break;
                            case "resourcepack":
                                resourcepacks.put(entry.getKey(), info);
                                break;
                            case "recommend":
                                recommend.put(entry.getKey(), info);
                                break;
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
            if(checkMod(info.id)) {
                List<Info> infos = new ArrayList<>();
                return infos;
            }
        }
        return new ArrayList<>(ctm.values());
    }
    //返回没有安装的部分！
    public static List<Info> checkResourcePacks(){
        return resourcepacks.values().stream().filter(info->!checkResourceFile(info.id)).collect(Collectors.toList());
    }

    private static final String MOD_ID_INDIUM = "indium";
    private static final String MOD_ID_SODIUM = "sodium";
    private static final String MOD_ID_YUUSHYA_MODELLING = "yuushya_modelling";
    private static final String MOD_ID_CONTINUITY = "continuity";
    public static List<Info> checkRecommend(){
        List<Info> list = recommend.values().stream().filter(info -> !MOD_ID_INDIUM.equals(info.id) && !checkMod(info.id)).collect(Collectors.toList());
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
        for(String selectedId : minecraft.getResourcePackRepository().getSelectedIds()){
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
