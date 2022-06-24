package net.cocofish.yuushya.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.utils.YuushyaLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class YuushyaResourceReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON =new GsonBuilder().create();

    public YuushyaResourceReloadListener() {
        super(GSON, "register");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        map.forEach((key,value)->{
            YuushyaLogger.info(key.toString());
            YuushyaLogger.info(value.toString());
            YuushyaRegistryData yuushyaRegistryData= GSON.fromJson(value,YuushyaRegistryData.class);
            yuushyaRegistryData.block.forEach((e)->{
                YuushyaLogger.info(e.name);
            });
            yuushyaRegistryData.item.forEach((e)->{
                YuushyaLogger.info(e.name);
            });
        });
    }
}
