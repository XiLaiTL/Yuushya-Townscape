package com.yuushya.collision;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yuushya.collision.data.Collision;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.collision.utils.VoxelShapeUtils;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaModelUtils;
import dev.architectury.platform.Platform;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
    public static void readAll(){
        if(Files.exists(COLLISION_FILES)){
            try(DirectoryStream<Path> paths = Files.newDirectoryStream(COLLISION_FILES)){
                for(Path path:paths){
                    String namespace = path.getFileName().toString();
                    Path newPath = path.resolve("./data/"+namespace+"/collision/");
                    try (DirectoryStream<Path> collisionFiles = Files.newDirectoryStream(newPath)) {
                        for(Path collisionFile:collisionFiles){
                            //String id = collisionFile.getFileName().toString().replace(".json","");
                            if(Files.exists(collisionFile)){
                                try(JsonReader reader= new JsonReader(new BufferedReader(new FileReader(collisionFile.toFile(), StandardCharsets.UTF_8))) ){
                                    //reader.setLenient(true);
                                    CollisionItem collision = NormalGSON.fromJson(JsonParser.parseReader(reader),CollisionItem.class);
                                    CollisionMap.put(new ResourceLocation(collision.namespace,collision.id).toString(),collision);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) { e.printStackTrace();}
        }
    }
    public static void readin(){
        for(String key:CollisionMap.keySet()){
            readone(key);
        }
    }

    public static void readone(String namespaceid){
        CollisionItem collision = CollisionMap.get(namespaceid);
        if (collision.parent!=null){
            ResourceLocation parent = new ResourceLocation(collision.namespace,collision.parent);
            collision.blockstates=CollisionMap.get(parent.toString()).blockstates;
        }
        for(var variant:collision.blockstates){
            List<BlockState> blockstates = YuushyaModelUtils.getBlockStateFromVariantString(YuushyaRegistries.BLOCKS.get(collision.id).get(),variant.variant);
            VoxelShape shape = getVoxelShape(variant);
            for(var blockstate:blockstates){
                YuushyaVoxelShapes.put(blockstate,shape);
            }
        };
    }

    public static VoxelShape getVoxelShape(CollisionItem.Model model){
        VoxelShape shape = Shapes.empty();
        for(var cube:model.collision){
            shape=Shapes.or(shape,Shapes.box(cube.from.get(0)/16.0,cube.from.get(1)/16.0,cube.from.get(2)/16.0,cube.to.get(0)/16.0,cube.to.get(1)/16.0,cube.to.get(2)/16.0));
        }
        shape = rotate(shape, model.x, model.y);
        if(shape.optimize().isEmpty()){
            return Shapes.block();
        }
        else{ return shape.optimize();}
    }
    public static VoxelShape rotate(VoxelShape shape,Integer x,Integer y){
        if(x!=null){
            switch (x){
                case 270:shape=VoxelShapeUtils.rotate(shape, Direction.SOUTH);
                case 180:shape=VoxelShapeUtils.rotate(shape, Direction.SOUTH);
                case 90: shape=VoxelShapeUtils.rotate(shape, Direction.SOUTH);
                case 0:  break;
            }
        }
        if(y!=null){
            switch (y){
                case 0:  break;
                case 90: shape=VoxelShapeUtils.rotate(shape, Rotation.CLOCKWISE_90);       break;
                case 180:shape=VoxelShapeUtils.rotate(shape, Rotation.CLOCKWISE_180);      break;
                case 270:shape=VoxelShapeUtils.rotate(shape, Rotation.COUNTERCLOCKWISE_90);break;
            }
        }
        return shape;
    }
}
