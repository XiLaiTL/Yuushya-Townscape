package com.yuushya.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuushya.Yuushya;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistryData;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.yuushya.Yuushya.MOD_ID;
import static com.yuushya.collision.CollisionFileReader.getCollisionMap;
import static com.yuushya.collision.CollisionFileReader.getVoxelShape;
import static com.yuushya.registries.YuushyaRegistryConfig.*;
import static com.yuushya.utils.GsonTools.NormalGSON;

//最主要的是读取两种文件：data/register/xxx.json, data/collision/xxx.json
public class AddonLoader {
    private static final Predicate<Path> ADDON_FILTER = path -> !Files.isDirectory(path) && path.getFileName().toString().endsWith("."+MOD_ID+".jar");
    private static final Predicate<ResourceLocation> JSON_FILTER = resourceLocation -> resourceLocation.getPath().endsWith(".json");
    //只读取yuushya文件夹了现在
    private static final FallbackResourceManager YUUSHYA_MANAGER = new FallbackResourceManager(PackType.SERVER_DATA,MOD_ID);

    private static Path classJarPath(String modId, Class<?> ...clazz){
        if(modId!=null && Platform.getModIds().contains(modId)){
            List<Path> paths = Platform.getMod(modId).getFilePaths();
            if(paths!=null&&!paths.isEmpty()){
                for(Path res:paths) if(!res.toString().isEmpty()&&Files.isRegularFile(res)){
                    return res;
                }
            }
        }
        for(Class<?> cls:clazz) {
            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                URL jarPath = codeSource.getLocation();
                String decoded = URLDecoder.decode(jarPath.getPath(), StandardCharsets.UTF_8);
                String dir = new File(decoded).getPath().replaceAll("#.+!", "").replaceAll("\\.jar.+", ".jar");
                return Paths.get(dir);
            }
        }
        return null;
    }
    public static void loadResource(String modId, Class<?> ...clazz){
        Path path = classJarPath(modId, clazz);
        if(path!=null && Files.exists(path)){
            try (PackResources packResource = Files.isDirectory(path)
                    ? new PathPackResources(new PackLocationInfo(path.getFileName().toString(), Component.empty(), PackSource.DEFAULT, Optional.empty()), path)
                    : new FilePackResources.FileResourcesSupplier(path).openPrimary(new PackLocationInfo(path.getFileName().toString(), Component.empty(), PackSource.DEFAULT, Optional.empty()))){
                YUUSHYA_MANAGER.push(packResource);
            }
        }
    }

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

    private static <V> List<Map.Entry<ResourceLocation,V>> sortMapEntry(Set<Map.Entry<ResourceLocation,V>> entrySet){
        return entrySet.stream().sorted(
                Comparator.comparing((Map.Entry<ResourceLocation, V> a) -> a.getKey().getPath())
                .thenComparing(a -> a.getKey().getNamespace())).toList();
    }
    public static void getRegister() {
        for (Map.Entry<ResourceLocation, Resource> entry : sortMapEntry(YUUSHYA_MANAGER.listResources("register", JSON_FILTER).entrySet())) { try {
            Resource resource = entry.getValue();
            try (BufferedReader reader = new BufferedReader(resource.openAsReader());) {
                JsonElement innerJson= JsonParser.parseReader(reader);
                mergeYuushyaRegistryBlockClass(innerJson);
                YuushyaRegistryData YuushyaData=NormalGSON.fromJson(innerJson, YuushyaRegistryData.class);
                addResultToRawMap(YuushyaData);
            }
        } catch (IOException e){ e.printStackTrace(); }}
    }

    public static void getCollision(){
        for (Map.Entry<ResourceLocation, Resource> entry : sortMapEntry(YUUSHYA_MANAGER.listResources("collision", JSON_FILTER).entrySet())) { try {
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
