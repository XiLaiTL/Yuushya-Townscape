package com.yuushya.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.yuushya.Yuushya;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YuushyaResourceReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON =new GsonBuilder().create();

    public YuushyaResourceReloadListener() {
        super(GSON, "register");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        map.forEach((fileResourceLocationString,json)->{
            YuushyaRegistryData yuushyaRegistryData= GSON.fromJson(json,YuushyaRegistryData.class);
            yuushyaRegistryData.block.forEach((e)->YuushyaRawBlockMap.put(e.name,e));
            yuushyaRegistryData.item.forEach((e)->YuushyaRawItemMap.put(e.name,e));
            yuushyaRegistryData.particle.forEach((e)->YuushyaRawParticleMap.put(e.name,e));
        });
        //YuushyaRegistryConfig.WriteRegistryConfig();

    }

    public static final Map<String,YuushyaRegistryData.Block> YuushyaRawBlockMap=new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Item> YuushyaRawItemMap = new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Particle> YuushyaRawParticleMap =new HashMap<>();
    public static YuushyaRegistryData getYuushyaRegistryData(){
        YuushyaRegistryData yuushyaRegistryData=new YuushyaRegistryData();
        yuushyaRegistryData.block= (List<YuushyaRegistryData.Block>) YuushyaResourceReloadListener.YuushyaRawBlockMap.values();
        yuushyaRegistryData.item= (List<YuushyaRegistryData.Item>) YuushyaResourceReloadListener.YuushyaRawItemMap.values();
        yuushyaRegistryData.particle= (List<YuushyaRegistryData.Particle>) YuushyaResourceReloadListener.YuushyaRawParticleMap.values();
        yuushyaRegistryData.version= YuushyaRegistryConfig.VERSION;
        return yuushyaRegistryData;
    }

}
