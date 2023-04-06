package com.yuushya.collision;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuushya.collision.data.BlockState;
import com.yuushya.collision.data.Collision;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.collision.data.Model;
import com.yuushya.collision.utils.CollisionCalc;
import com.yuushya.datagen.utils.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

import static com.yuushya.datagen.ConfigReader.TemplateBrother;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileCreator {

    private final Path _basePath;
    private final Path _resPath;
    private final String _nameSpace ;
    private static final CollisionItem.Model.Element CUBE = new CollisionItem.Model.Element(0.0,0.0,0.0,16.0,16.0,16.0);
    private final Collision collisionData = new Collision();
    public CollisionFileCreator(String nameSpace,Path basePath){
        this._nameSpace = nameSpace;
        this._resPath =  Path.of("../config/com.yuushya/"+nameSpace+"/");
        this._basePath=basePath;
    }
    private void readBlockStateAndModel(){
        Path path = _basePath.resolve("./assets/"+ _nameSpace +"/blockstates/");//read the all blockstates under the namespace
        ModelReader modelReader = new ModelReader(this._basePath);
        collisionData.items = new ArrayList<>();//这里重新定义了！！
        if(path.toFile().listFiles()!=null){
            for(var blockstateFile:path.toFile().listFiles() ) {
                CollisionItem item = new CollisionItem();
                item.namespace = _nameSpace;
                item.id = blockstateFile.getName().replace(".json","");
                if(TemplateBrother.containsKey(item.id)&&!TemplateBrother.get(item.id).equals(item.id)){
                    item.parent = TemplateBrother.get(item.id);
                }
                else{
                    item.blockstates = new ArrayList<>();
                    try (BufferedReader reader=new BufferedReader(new FileReader(blockstateFile))){
                        JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                        if(obj.has("variants")) {//先不做multipart了
                            for(var pair:obj.getAsJsonObject("variants").entrySet()){
                                CollisionItem.Model itemBlockstate = new CollisionItem.Model();
                                itemBlockstate.collision = new ArrayList<>();
                                itemBlockstate.variant = pair.getKey();//.isEmpty()? "empty": pair.getKey().replace("=","#");
                                JsonElement value = pair.getValue();
                                if(value.isJsonArray()){ value = value.getAsJsonArray().get(0); }
                                BlockState.Variant variant = NormalGSON.fromJson(value, BlockState.Variant.class);
                                itemBlockstate.x = variant.x;
                                itemBlockstate.y = variant.y;

                                Model model = modelReader.read(new ResourceLocation(variant.model));
                                if(model.elements==null){
                                    itemBlockstate.collision.add(CUBE);
                                }
                                else{
                                    for(var el:model.elements){
                                        itemBlockstate.collision.add( CollisionCalc.getPointList(el));
                                    }
                                    item.blockstates.add(itemBlockstate);
                                }
                            }
                        }
                    }catch (IOException e){e.printStackTrace();}
                }
                collisionData.items.add(item);
            }
        }
    }

    private void writeCollision(){
        Path path = this._resPath.resolve("./data/"+this._nameSpace+"/collision/");
        path.toFile().mkdirs();
        for(var item:collisionData.items){
            Path itemPath = path.resolve(item.id+".json");
            try(BufferedWriter writer=new BufferedWriter( new FileWriter(itemPath.toFile(), StandardCharsets.UTF_8))){
                String json=NormalGSON.toJson(item);
                json= json.replaceAll("((?<=[,\\[])\n\s*(?=[-0-9]|\"to\"))|((?<=[0-9])\n\s*(?=]))|((?<=\\{)\n\s*(?=\"from\"))"," ").replaceAll("((?<=([0-9] ]))\n\s*(?=}))"," ");
                writer.write(json);
            }catch (IOException e){ e.printStackTrace();}
        }
    }
    public void create(){
        readBlockStateAndModel();
        writeCollision();
    }
}
