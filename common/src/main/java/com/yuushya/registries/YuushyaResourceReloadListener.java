package com.yuushya.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.yuushya.Yuushya;
import com.yuushya.utils.YuushyaLogger;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YuushyaResourceReloadListener extends SimpleJsonResourceReloadListener {
    public YuushyaResourceReloadListener() {
        super(YuushyaUtils.NormalGSON, "register");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        map.forEach((fileResourceLocationString,json)->{
            YuushyaRegistryData yuushyaRegistryData= YuushyaUtils.NormalGSON.fromJson(json,YuushyaRegistryData.class);
            yuushyaRegistryData.block.forEach((e)->YuushyaRawBlockMap.put(e.name,e));
            yuushyaRegistryData.item.forEach((e)->YuushyaRawItemMap.put(e.name,e));
            yuushyaRegistryData.particle.forEach((e)->YuushyaRawParticleMap.put(e.name,e));
        });
        YuushyaRegistryConfig.writeRegistryConfig();

    }

    public static final Map<String,YuushyaRegistryData.Block> YuushyaRawBlockMap=new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Item> YuushyaRawItemMap = new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Particle> YuushyaRawParticleMap =new HashMap<>();
    public static YuushyaRegistryData getYuushyaRegistryData(){
        YuushyaRegistryData yuushyaRegistryData=new YuushyaRegistryData();
        yuushyaRegistryData.block=  YuushyaResourceReloadListener.YuushyaRawBlockMap.values().stream().toList();
        yuushyaRegistryData.item= YuushyaResourceReloadListener.YuushyaRawItemMap.values().stream().toList();
        yuushyaRegistryData.particle= YuushyaResourceReloadListener.YuushyaRawParticleMap.values().stream().toList();
        yuushyaRegistryData.version= YuushyaRegistryConfig.VERSION;
        return yuushyaRegistryData;
    }

}
