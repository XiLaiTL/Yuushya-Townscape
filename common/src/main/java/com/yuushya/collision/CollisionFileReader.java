package com.yuushya.collision;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaModelUtils;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuushya.block.YuushyaBlockFactory.YuushyaVoxelShapes;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileReader {

    public static Map<String, CollisionItem> CollisionMap = new HashMap<>();
    public static Path COLLISION_FILES = Platform.getConfigFolder().resolve("./com.yuushya/");
    public static void readAllFile(){
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
                                    CollisionMap.put(new ResourceLocation(namespace,id).toString(),collision);
                                    if(collision.children!=null){
                                        for(String namespaceId:collision.children){
                                            CollisionMap.put(namespaceId,collision);
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
        CollisionMap.forEach((key, value) -> readCollisionToVoxelShape(key));
    }

    public static void readCollisionToVoxelShape(String namespaceid){
        CollisionItem collision = CollisionMap.get(namespaceid);
        if(collision!=null&&collision.blockstates!=null){
            for(var variant:collision.blockstates){
                List<BlockState> blockstates = YuushyaModelUtils.getBlockStateFromVariantString(YuushyaRegistries.BLOCKS.get(new ResourceLocation(namespaceid).getPath()).get(),variant.variant);
                VoxelShape shape = getVoxelShape(variant);
                for(var blockstate:blockstates){
                    YuushyaVoxelShapes.put(blockstate,shape);
                }
            };
        }
    }

    public static VoxelShape getVoxelShape(CollisionItem.Model model){
        VoxelShape shape = Shapes.empty();
        for(var cube:model.collision){
            shape=Shapes.or(shape,Shapes.box(cube.from.get(0)/16.0,cube.from.get(1)/16.0,cube.from.get(2)/16.0,cube.to.get(0)/16.0,cube.to.get(1)/16.0,cube.to.get(2)/16.0));
        }
        shape = shape.optimize();
        if(shape.isEmpty()){ return Shapes.block();}
        else{ return shape;}
    }

}
