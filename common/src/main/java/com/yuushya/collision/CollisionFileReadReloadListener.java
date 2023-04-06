package com.yuushya.collision;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaModelUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.lwjgl.system.CallbackI;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.yuushya.block.YuushyaBlockFactory.YuushyaVoxelShapes;
import static com.yuushya.collision.CollisionFileReader.*;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileReadReloadListener implements ResourceManagerReloadListener {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        for(ResourceLocation resourceLocation:resourceManager.listResources("collision",path->path.endsWith(".json"))){
            try(InputStream stream = resourceManager.getResource(resourceLocation).getInputStream()) {
                JsonElement json = JsonParser.parseReader( new InputStreamReader(new BufferedInputStream(stream) ));
                CollisionItem collision = NormalGSON.fromJson(json,CollisionItem.class);
                CollisionMap.put(new ResourceLocation(collision.namespace,collision.id).toString(),collision);
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}
