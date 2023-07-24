package com.yuushya.registries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.yuushya.registries.YuushyaRegistryConfig.*;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class YuushyaResourceReloadListener extends SimpleJsonResourceReloadListener {
    public YuushyaResourceReloadListener() {
        super(NormalGSON, "register");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        map.forEach((fileResourceLocationString,json)->{
            JsonArray block=json.getAsJsonObject().getAsJsonArray("block");
            JsonArray item=json.getAsJsonObject().getAsJsonArray("item");
            JsonArray particle=json.getAsJsonObject().getAsJsonArray("particle");
            if (block!=null) block.forEach((e)-> YuushyaRawJsonBlockMap.put(e.getAsJsonObject().getAsJsonPrimitive("name").getAsString(),e.getAsJsonObject()));
            if (item!=null) item.forEach((e)-> YuushyaRawJsonItemMap.put(e.getAsJsonObject().getAsJsonPrimitive("name").getAsString(),e.getAsJsonObject()));
            if (particle!=null) particle.forEach((e)-> YuushyaRawJsonParticleMap.put(e.getAsJsonObject().getAsJsonPrimitive("name").getAsString(),e.getAsJsonObject()));
        });
        writeRegistryConfig();

    }
    public static final Map<String, JsonObject> YuushyaRawJsonBlockMap =new LinkedHashMap<>();
    public static final Map<String,JsonObject> YuushyaRawJsonItemMap = new LinkedHashMap<>();
    public static final Map<String,JsonObject> YuushyaRawJsonParticleMap =new LinkedHashMap<>();
    public static YuushyaRegistryData getYuushyaRegistryData(){
        JsonArray block=new JsonArray();YuushyaRawJsonBlockMap.values().forEach(block::add);
        JsonArray item=new JsonArray();YuushyaRawJsonItemMap.values().forEach(item::add);
        JsonArray particle=new JsonArray();YuushyaRawJsonParticleMap.values().forEach(particle::add);
        mergeYuushyaRegistryBlockJson(block);
        JsonObject json=new JsonObject();
        json.add("block",block);
        json.add("item",item);
        json.add("particle",particle);
        YuushyaRegistryData yuushyaRegistryData=NormalGSON.fromJson(json,YuushyaRegistryData.class);
        yuushyaRegistryData.version= VERSION;
        return yuushyaRegistryData;
    }
    public static void writeRegistryConfig(){
        try{
            CONFIG_FILE.getParentFile().mkdirs();
            CONFIG_FILE.createNewFile();
            YuushyaRegistryData yuushyaRegistryData=YuushyaResourceReloadListener.getYuushyaRegistryData();
            String json= NormalGSON.toJson(yuushyaRegistryData);
            try(BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8))){
                writer.write(json);
            }catch (IOException e){e.printStackTrace();}
        }catch (IOException e){e.printStackTrace();}
    }
}
