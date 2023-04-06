package com.yuushya.collision;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaModelUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Map;

import static com.yuushya.block.YuushyaBlockFactory.YuushyaVoxelShapes;
import static com.yuushya.collision.CollisionFileReader.*;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileReadReloadListener extends SimpleJsonResourceReloadListener {


    public CollisionFileReadReloadListener() {
        super(NormalGSON,"collision");
    }

    public static List<String> lazyList;

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        map.forEach((resourceLocation,json)->{
            CollisionItem collision = NormalGSON.fromJson(json,CollisionItem.class);
            CollisionMap.put(new ResourceLocation(collision.namespace,collision.id).toString(),collision);
        });
        map.forEach((resourceLocation,json)->{
            readone(resourceLocation.toString());
            System.out.println(resourceLocation.toString());
        });


    }

}
