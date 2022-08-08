package com.yuushya.registries;

import com.google.gson.*;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.yuushya.registries.YuushyaRegistryConfig.mergeYuushyaRegistryBlockJson;

public class YuushyaResourceReloadListener extends SimpleJsonResourceReloadListener {
    public YuushyaResourceReloadListener() {
        super(YuushyaUtils.NormalGSON, "register");
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
        YuushyaRegistryConfig.writeRegistryConfig();

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
        YuushyaRegistryData yuushyaRegistryData=YuushyaUtils.NormalGSON.fromJson(json,YuushyaRegistryData.class);
        yuushyaRegistryData.version= YuushyaRegistryConfig.VERSION;
        return yuushyaRegistryData;
    }

}
