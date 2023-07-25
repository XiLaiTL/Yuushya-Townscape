
package com.yuushya.collision;

import com.google.gson.JsonElement;
import com.yuushya.block.YuushyaBlockFactory;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;

import static com.yuushya.collision.CollisionFileReader.getCollisionMap;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileReadReloadListener extends SimpleJsonResourceReloadListener {


    public CollisionFileReadReloadListener() {
        super(NormalGSON, "collision");
    }

    public static List<String> lazyList;

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        YuushyaLogger.info("collisionReader1"+YuushyaBlockFactory.getYuushyaVoxelShapes().size());
        map.forEach((resourceLocation, json) -> {
            CollisionItem collision = NormalGSON.fromJson(json, CollisionItem.class);
            getCollisionMap().put(resourceLocation.toString(), collision);
            if(collision.children!=null){
                for(String namespaceId:collision.children){
                    getCollisionMap().put(namespaceId,collision);
                }
            }

        });
        CollisionFileReader.readAllCollision();
        YuushyaLogger.info("collisionReader2"+YuushyaBlockFactory.getYuushyaVoxelShapes().size());
    }
}