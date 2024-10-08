package com.yuushya.collision;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yuushya.Yuushya;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaLogger;
import com.yuushya.utils.YuushyaModelUtils;
import com.yuushya.utils.YuushyaUtils;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
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

import static com.yuushya.block.YuushyaBlockFactory.getYuushyaCollisionShapes;
import static com.yuushya.block.YuushyaBlockFactory.getYuushyaVoxelShapes;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileReader {
    private static Map<String, Map<String,VoxelShape>> collisionMap = new HashMap<>();
    public static Path COLLISION_FILES = Platform.getConfigFolder().resolve("./com.yuushya/");

    @Deprecated
    public static void readAllFileSelf(){
        Set<String> set=  YuushyaRegistries.BlockALL.keySet();
        for(String name : set){

            try (InputStream inputStream = CollisionFileReader.class.getResourceAsStream("/data/yuushya/collision/" + name + ".json")) {
                if(inputStream!=null){
                    JsonElement jsonElement =JsonParser.parseReader( new BufferedReader(new InputStreamReader(inputStream)));
                    CollisionItem collision = NormalGSON.fromJson(jsonElement ,CollisionItem.class);
                    if (collision != null && collision.blockstates != null){
                        Map<String,VoxelShape> map = new HashMap<>();
                        for(CollisionItem.Model variant:collision.blockstates){
                            map.put(variant.variant,getVoxelShape(variant));
                        }
                        getCollisionMap().put(new ResourceLocation(Yuushya.MOD_ID,name).toString(),map);
                        if(collision.children!=null){
                            for(String namespaceId:collision.children){
                                getCollisionMap().put(namespaceId,map);
                            }
                        }
                    }
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    @Deprecated
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
                                try(JsonReader reader= new JsonReader(new BufferedReader(new FileReader(collisionFile.toFile(), StandardCharsets.UTF_8))) ){
                                    //reader.setLenient(true);
                                    CollisionItem collision = NormalGSON.fromJson(JsonParser.parseReader(reader),CollisionItem.class);
                                    if (collision != null && collision.blockstates != null){
                                        Map<String,VoxelShape> map = new HashMap<>();
                                        for(CollisionItem.Model variant:collision.blockstates){
                                            map.put(variant.variant,getVoxelShape(variant));
                                        }
                                        getCollisionMap().put(new ResourceLocation(namespace,id).toString(),map);
                                        if(collision.children!=null){
                                            for(String namespaceId:collision.children){
                                                getCollisionMap().put(namespaceId,map);
                                            }
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
        if(getYuushyaVoxelShapes().isEmpty()){
            getCollisionMap().forEach((key, value) -> readCollisionToVoxelShape(key));
        }
    }

    public static void readCollisionToVoxelShape(BlockState blockState,String namespaceid){
        if(! (blockState.getBlock() instanceof AirBlock)) {
            Map<String,VoxelShape> collision = getCollisionMap().get(namespaceid);
            if (collision!=null) {
                for(String variant:collision.keySet()){
                    if(YuushyaModelUtils.isBlockStateInVariantString(blockState,variant)){
                        VoxelShape shape = collision.get(variant);
                        String id = blockState.toString();
                        getYuushyaVoxelShapes().put(id, shape);
                        getYuushyaCollisionShapes().put(id, restrictShape(shape));
                    }
                }
            }
        }
    }

    public static void readCollisionToVoxelShape(String namespaceid){
        Map<String,VoxelShape> collision = getCollisionMap().get(namespaceid);
        if(collision!=null) {
            Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(namespaceid));
            if (!(block instanceof AirBlock)) {
                for (var entry : collision.entrySet()) {
                    String variant = entry.getKey();
                    VoxelShape shape = entry.getValue();
                    List<BlockState> blockstates = YuushyaModelUtils.getBlockStateFromVariantString(block, variant);
                    for (var blockstate : blockstates) {
                        getYuushyaVoxelShapes().put(blockstate.toString(), shape);
                        getYuushyaCollisionShapes().put(blockstate.toString(), restrictShape(shape));
                    }
                }
            }
        }
    }

    public static VoxelShape getVoxelShape(CollisionItem.Model model){
        VoxelShape shape = Shapes.empty();
        if(model.collision == null) return Shapes.block();
        for(var cube:model.collision){
            shape=Shapes.or(shape, Block.box(cube.from.get(0),cube.from.get(1),cube.from.get(2),cube.to.get(0),cube.to.get(1),cube.to.get(2)));
        }
        shape = shape.optimize();
        if(shape.isEmpty()){ return Shapes.block();}
        else{ return shape;}
    }

    private static final Map<VoxelShape,VoxelShape> RESTRICT_SHAPE_MAP = new HashMap<>();
    public static VoxelShape restrictShape(VoxelShape shape){
        if(RESTRICT_SHAPE_MAP.containsKey(shape)) return RESTRICT_SHAPE_MAP.get(shape);
        VoxelShape res = Shapes.empty();
        for(var aabb: shape.toAabbs()){
            VoxelShape one = Shapes.create(
                    Math.max(0,aabb.minX),Math.max(0,aabb.minY),Math.max(0,aabb.minZ),
                    Math.min(1,aabb.maxX),Math.min(1.5,aabb.maxY),Math.min(1,aabb.maxZ)
            );
            res = Shapes.or(res,one);
        }
        res = res.optimize();
        RESTRICT_SHAPE_MAP.put(shape,res);
        return res;
    }

    public static Map<String, Map<String, VoxelShape>> getCollisionMap() {
        return collisionMap;
    }

    public static void setCollisionMap(Map<String, Map<String, VoxelShape>> collisionMap) {
        CollisionFileReader.collisionMap=collisionMap;
    }
}
