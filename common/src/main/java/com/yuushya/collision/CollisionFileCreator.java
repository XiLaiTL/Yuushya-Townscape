package com.yuushya.collision;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuushya.collision.data.BlockState;
import com.yuushya.collision.data.Collision;
import com.yuushya.collision.data.Model;
import com.yuushya.collision.utils.CollisionCalc;
import com.yuushya.datagen.utils.ResourceLocation;

import java.io.*;
import java.nio.file.Path;

import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileCreator {

    private final Path _basePath;
    private final Path _resPath;
    private final String _nameSpace ;
    private final Collision collisionData = new Collision();
    public CollisionFileCreator(String nameSpace,Path basePath){
        this._nameSpace = nameSpace+"_output";
        this._resPath =  Path.of("../config/com.yuushya/"+nameSpace+"/");
        this._basePath=basePath;
    }
    private void readBlockStateAndModel(){
        Path path = _basePath.resolve("/"+ _nameSpace +"/blockstates");//read the all blockstates under the namespace
        ModelReader modelReader = new ModelReader(this._basePath);
        if(path.toFile().listFiles()!=null){
            for(var blockstateFile:path.toFile().listFiles() ) {
                Collision.Item item = new Collision.Item();
                item.namespace = _nameSpace;
                item.id = blockstateFile.getName().replace(".json","");
                try (BufferedReader reader=new BufferedReader(new FileReader(blockstateFile))){
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    if(obj.has("variants")) {//先不做multipart了
                        for(var pair:obj.getAsJsonObject("variants").entrySet()){
                            Collision.Item.Model itemBlockstate = new Collision.Item.Model();
                            itemBlockstate.variant = pair.getKey();
                            JsonElement value = pair.getValue();
                            if(value.isJsonArray()){ value = value.getAsJsonArray().get(0); }
                            BlockState.Variant variant = NormalGSON.fromJson(value, BlockState.Variant.class);
                            itemBlockstate.x = variant.x;
                            itemBlockstate.y = variant.y;
                            Model model = modelReader.read(new ResourceLocation(variant.model));
                            for(var el:model.elements){
                                itemBlockstate.collision.add( CollisionCalc.getPointList(el));
                            }
                            item.blockstates.add(itemBlockstate);
                        }
                    }
                }catch (IOException e){e.printStackTrace();}
                collisionData.items.add(item);
            }
        }
    }

    private void writeCollision(){
        Path path = this._resPath.resolve("/data/"+this._nameSpace+"/collision/");
        path.toFile().mkdirs();
        for(var item:collisionData.items){
            Path itemPath = path.resolve(item.id+".json");
            try(BufferedWriter writer=new BufferedWriter( new FileWriter(itemPath.toFile()))){
                writer.write(NormalGSON.toJson(item));
            }catch (IOException e){ e.printStackTrace();}
        }
    }
    public void create(){
        readBlockStateAndModel();
        writeCollision();
    }
}
