package com.yuushya.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuushya.Yuushya;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistryData;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yuushya.Yuushya.MOD_ID;
import static com.yuushya.collision.CollisionFileReader.getCollisionMap;
import static com.yuushya.collision.CollisionFileReader.getVoxelShape;
import static com.yuushya.registries.YuushyaRegistryConfig.*;
import static com.yuushya.utils.GsonTools.NormalGSON;

//最主要的是读取两种文件：data/register/xxx.json, data/collision/xxx.json
public class AddonLoader {
    private static final Predicate<Path> ADDON_FILTER = path -> !Files.isDirectory(path) && (path.getFileName().toString().endsWith("."+MOD_ID+".jar")||path.getFileName().toString().endsWith("."+MOD_ID+".zip"));
    private static final Predicate<String> JSON_FILTER = string -> string.endsWith(".json");
    //只读取yuushya文件夹了现在
    private static final FallbackResourceManager YUUSHYA_MANAGER = new FallbackResourceManager(PackType.SERVER_DATA,MOD_ID);

    private static Path classJarPath(String modId, Class<?> ...clazz){
        if(modId!=null && Platform.getModIds().contains(modId)){
            List<Path> paths = Platform.getMod(modId).getFilePaths();
            if(paths!=null&&!paths.isEmpty()){
                for(Path res:paths) if(!res.toString().isEmpty()){
                    return res;
                }
            }
        }
        for(Class<?> cls:clazz){
            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            if(codeSource!=null){
                URL jarPath = codeSource.getLocation();
                String decoded = null;
                try {
                    decoded = URLDecoder.decode(jarPath.getPath(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    decoded = jarPath.getPath().replaceAll("%20","");
                }
                String dir = new File(decoded).getPath().replaceAll("#.+!","").replaceAll("\\.jar.+",".jar");
                return Paths.get(dir);
            }
        }
        return null;
    }
    public static void loadResource(String modId, Class<?> ...clazz){
        Path path = classJarPath(modId,clazz);
        if(path!=null && Files.exists(path)){
            try (PackResources packResource = Files.isDirectory(path)
                    ? new FolderPackResources(path.toFile())
                    : new FilePackResources(path.toFile())){
                YUUSHYA_MANAGER.add(packResource);
            }
        }
    }

    public static void loadPackResource(Path folder){
        try(Stream<Path> files = Files.list(folder).filter(ADDON_FILTER).sorted(Comparator.comparing(Path::toString)) ){
            //TODO: 可以用Pack来包装，做一个addon管理Screen，见 PackSelectionScreen
            List<Path> fileList = files.collect(Collectors.toList());
            for(Path path:fileList){
                try (PackResources packResource = new FilePackResources(path.toFile())){
                    YUUSHYA_MANAGER.add(packResource);
                }
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    private static <V> List<ResourceLocation> sortMapEntry(Collection<ResourceLocation> entrySet){
        return entrySet.stream().sorted(
                Comparator.comparing(ResourceLocation::getPath)
                .thenComparing(ResourceLocation::getNamespace)).collect(Collectors.toList());
    }
    public static void getRegister() {
        for (ResourceLocation file : sortMapEntry(YUUSHYA_MANAGER.listResources("register", JSON_FILTER))) { try {
                for (Resource resource : YUUSHYA_MANAGER.getResources(file)) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));) {
                        JsonElement innerJson= new JsonParser().parse(reader);
                        mergeYuushyaRegistryBlockClass(innerJson);
                        YuushyaRegistryData YuushyaData=NormalGSON.fromJson(innerJson, YuushyaRegistryData.class);
                        addResultToRawMap(YuushyaData);
                    }
                }
        } catch (IOException e){ e.printStackTrace(); }}
    }

    public static void getCollision(){
        for (ResourceLocation file : YUUSHYA_MANAGER.listResources("collision", JSON_FILTER)) { try {
            ResourceLocation namespaceId1 = new ResourceLocation(Yuushya.MOD_ID, file.getPath().substring("collision/".length(), file.getPath().length() - ".json".length()));
            for (Resource resource : YUUSHYA_MANAGER.getResources(file)) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));) {
                    JsonElement jsonElement = new JsonParser().parse(reader);
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
            }

        } catch (IOException e){ e.printStackTrace(); }}
    }



}
