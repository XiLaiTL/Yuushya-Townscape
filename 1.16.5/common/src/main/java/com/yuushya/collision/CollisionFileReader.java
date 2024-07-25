package com.yuushya.collision;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yuushya.Yuushya;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaModelUtils;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yuushya.block.YuushyaBlockFactory.getYuushyaVoxelShapes;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileReader {

    private static Map<String, CollisionItem> collisionMap = new HashMap<>();
    public static Path COLLISION_FILES = Platform.getConfigFolder().resolve("./com.yuushya/");

    public static void readAllFileSelf(){
        Set<String> set=  YuushyaRegistries.BlockALL.keySet();
        for(String name : set){

            try (InputStream inputStream = CollisionFileReader.class.getResourceAsStream("/data/yuushya/collision/" + name + ".json")) {
                if(inputStream!=null){
                    JsonElement jsonElement =new JsonParser().parse( new BufferedReader(new InputStreamReader(inputStream)));
                    CollisionItem collision = NormalGSON.fromJson(jsonElement ,CollisionItem.class);
                    getCollisionMap().put(new ResourceLocation(Yuushya.MOD_ID,name).toString(),collision);
                    if(collision.children!=null){
                        for(String namespaceId:collision.children){
                            getCollisionMap().put(namespaceId,collision);
                        }
                    }
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    public static void readAllFileFromConfig(){
        if(Files.exists(COLLISION_FILES)){
            try(DirectoryStream<Path> paths = Files.newDirectoryStream(COLLISION_FILES)){
                for(Path path:paths){
                    String namespace = path.getFileName().toString();
                    Path newPath = path.resolve("./data/"+namespace+"/collision/");
                    try (DirectoryStream<Path> collisionFiles = Files.newDirectoryStream(newPath)) {
                        for(Path collisionFile:collisionFiles){
                            String id = collisionFile.getFileName().toString().replace(".json","");
                            if(Files.exists(collisionFile)){
                                try(JsonReader reader= new JsonReader(new BufferedReader(new FileReader(collisionFile.toFile()))) ){
                                    //reader.setLenient(true);
                                    CollisionItem collision = NormalGSON.fromJson(new JsonParser().parse(reader),CollisionItem.class);
                                    getCollisionMap().put(new ResourceLocation(namespace,id).toString(),collision);
                                    if(collision.children!=null){
                                        for(String namespaceId:collision.children){
                                            getCollisionMap().put(namespaceId,collision);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) { e.printStackTrace();}
        }
    }

    public static void readAllCollision(){
        getCollisionMap().forEach((key, value) -> readCollisionToVoxelShape(key));
    }

    public static void readCollisionToVoxelShape(Map<Integer,VoxelShape> cache,Block block,String namespaceid){
        if(! (block instanceof AirBlock)) {
            CollisionItem collision = getCollisionMap().get(namespaceid);
            if (collision != null && collision.blockstates != null) {for (CollisionItem.Model variant : collision.blockstates) {
                    List<BlockState> blockstates = YuushyaModelUtils.getBlockStateFromVariantString(block, variant.variant);
                    VoxelShape shape = getVoxelShape(variant);
                    for (BlockState blockstate : blockstates) {
                        getYuushyaVoxelShapes().put(Block.getId(blockstate), shape);
                        cache.put(Block.getId(blockstate),shape);
                    }
                }
            }
        }
    }
    public static void readCollisionToVoxelShape(String namespaceid){
        CollisionItem collision = getCollisionMap().get(namespaceid);
        if(collision!=null&&collision.blockstates!=null) {
            Block block = Registry.BLOCK.get(new ResourceLocation(namespaceid));
            if (!(block instanceof AirBlock)) {
                for (CollisionItem.Model variant : collision.blockstates) {
                    //RegistrySupplier<Block> block =YuushyaRegistries.BLOCKS.get(new ResourceLocation(namespaceid).getPath());
                    List<BlockState> blockstates = YuushyaModelUtils.getBlockStateFromVariantString(block, variant.variant);
                    VoxelShape shape = getVoxelShape(variant);
                    for (BlockState blockstate : blockstates) {
                        getYuushyaVoxelShapes().put(Block.getId(blockstate), shape);
                    }
                }
            }
        }
    }

    public static VoxelShape getVoxelShape(CollisionItem.Model model){
        VoxelShape shape = Shapes.empty();
        if(model.collision == null) return Shapes.block();
        for(CollisionItem.Model.Element cube:model.collision){
            shape=Shapes.or(shape, Block.box(cube.from.get(0),cube.from.get(1),cube.from.get(2),cube.to.get(0),cube.to.get(1),cube.to.get(2)));
        }
        shape = shape.optimize();
        if(shape.isEmpty()){ return Shapes.block();}
        else{ return shape;}
    }

    public static Map<String, CollisionItem> getCollisionMap() {
        return collisionMap;
    }

    public static void setCollisionMap(Map<String, CollisionItem> collisionMap) {
        CollisionFileReader.collisionMap=collisionMap;
    }
}
