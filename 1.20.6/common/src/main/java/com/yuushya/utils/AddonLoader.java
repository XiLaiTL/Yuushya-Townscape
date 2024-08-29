package com.yuushya.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuushya.Yuushya;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.yuushya.Yuushya.MOD_ID;
import static com.yuushya.collision.CollisionFileReader.getCollisionMap;
import static com.yuushya.collision.CollisionFileReader.getVoxelShape;
import static com.yuushya.registries.YuushyaRegistryConfig.addResultToRawMap;
import static com.yuushya.registries.YuushyaRegistryConfig.mergeYuushyaRegistryBlockJson;
import static com.yuushya.utils.GsonTools.NormalGSON;

//最主要的是读取两种文件：data/register/xxx.json, data/collision/xxx.json
public class AddonLoader {
    private static final Predicate<Path> ADDON_FILTER = path -> !Files.isDirectory(path) && path.getFileName().toString().endsWith("."+MOD_ID+".jar");
    private static final Predicate<ResourceLocation> JSON_FILTER = resourceLocation -> resourceLocation.getPath().endsWith(".json");
    //只读取yuushya文件夹了现在
    private static final FallbackResourceManager YUUSHYA_MANAGER = new FallbackResourceManager(PackType.SERVER_DATA,MOD_ID);

    public static void loadPackResource(Path folder){
        try(Stream<Path> files = Files.list(folder).filter(ADDON_FILTER).sorted(Comparator.comparing(Path::toString)) ){
            //TODO: 可以用Pack来包装，做一个addon管理Screen，见 PackSelectionScreen
            List<Path> fileList = files.toList();
            for(Path path:fileList){
                try (PackResources packResource = new FilePackResources.FileResourcesSupplier(path).openPrimary(new PackLocationInfo(path.getFileName().toString(), Component.empty(), PackSource.DEFAULT, Optional.empty()))){
                    YUUSHYA_MANAGER.push(packResource);
                }
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    public static void getRegister() {
        for (Map.Entry<ResourceLocation, Resource> entry : YUUSHYA_MANAGER.listResources("register", JSON_FILTER).entrySet()) { try {
            Resource resource = entry.getValue();
            try (BufferedReader reader = new BufferedReader(resource.openAsReader());) {
                JsonElement innerJson= JsonParser.parseReader(reader);
                mergeYuushyaRegistryBlockJson(innerJson.getAsJsonObject().getAsJsonArray("block"));
                YuushyaRegistryData YuushyaData=NormalGSON.fromJson(innerJson, YuushyaRegistryData.class);
                addResultToRawMap(YuushyaData);
            }
        } catch (IOException e){ e.printStackTrace(); }}
    }

    public static void getCollision(){
        for (Map.Entry<ResourceLocation, Resource> entry : YUUSHYA_MANAGER.listResources("collision", JSON_FILTER).entrySet()) { try {
            ResourceLocation file = entry.getKey();
            ResourceLocation namespaceId1 =new ResourceLocation(Yuushya.MOD_ID, file.getPath().substring("collision/".length(), file.getPath().length() - ".json".length()));
            Resource resource = entry.getValue();
            try (BufferedReader reader = new BufferedReader(resource.openAsReader())) {
                JsonElement jsonElement =JsonParser.parseReader(reader);
                CollisionItem collision = NormalGSON.fromJson(jsonElement ,CollisionItem.class);
                Map<String, VoxelShape> map = new HashMap<>();
                for(CollisionItem.Model variant:collision.blockstates){
                    map.put(variant.variant,getVoxelShape(variant));
                }
                getCollisionMap().put(namespaceId1.toString(),map);
                if(collision.children!=null){
                    for(String namespaceId:collision.children){
                        getCollisionMap().put(namespaceId,map);
                    }
                }
            }

        } catch (IOException e){ e.printStackTrace(); }}
    }



}
