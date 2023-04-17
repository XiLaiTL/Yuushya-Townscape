package com.yuushya.collision;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuushya.collision.data.BlockState;
import com.yuushya.collision.data.CollisionItem;
import com.yuushya.collision.data.Model;
import com.yuushya.collision.utils.OptimizeModel;
import com.yuushya.collision.utils.RotateModel;
import com.yuushya.datagen.utils.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.yuushya.datagen.ConfigReader.TemplateBrother;
import static com.yuushya.utils.GsonTools.NormalGSON;

public class CollisionFileCreator {

    private final Path _basePath;
    private Path _resPath;
    private final String _nameSpace ;
    private static final CollisionItem.Model.Element CUBE = new CollisionItem.Model.Element(0.0,0.0,0.0,16.0,16.0,16.0);
    private Map<String,CollisionItem> collisionData = new HashMap<>();
    public CollisionFileCreator(String nameSpace,Path basePath){
        this._nameSpace = nameSpace;
        this._resPath =  Path.of("../config/com.yuushya/"+nameSpace+"/");
        this._basePath=basePath;
    }
    private void readBlockStateAndModel(){
        Path path = _basePath.resolve("./assets/"+ _nameSpace +"/blockstates/");//read the all blockstates under the namespace
        ModelReader modelReader = new ModelReader(this._basePath);
        if(path.toFile().listFiles()!=null){
            for(var blockstateFile:path.toFile().listFiles() ) {

                ResourceLocation namespaceId = new ResourceLocation(_nameSpace,blockstateFile.getName().replace(".json",""));
                String namespaceIdString = namespaceId.toString();
                //这里没做namespaceId，默认是yuushya
                if(TemplateBrother.containsKey(namespaceId.getPath())&&!TemplateBrother.get(namespaceId.getPath()).equals(namespaceId.getPath())){
                    String parent = TemplateBrother.get(namespaceId.getPath());
                    String parentNamespaceId= new ResourceLocation(_nameSpace,parent).toString();
                    if(!collisionData.containsKey(parentNamespaceId)){
                        collisionData.put(parentNamespaceId,new CollisionItem());
                    }
                    if(collisionData.get(parentNamespaceId).children == null){collisionData.get(parentNamespaceId).children = new ArrayList<>(); }
                    collisionData.get(parentNamespaceId).children.add(namespaceIdString);
                }
                else{
                    CollisionItem item = new CollisionItem();
                    item.blockstates = new ArrayList<>();
                    try (BufferedReader reader=new BufferedReader(new FileReader(blockstateFile))){
                        JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                        if(obj.has("variants")) {//先不做multipart了
                            for(var pair:obj.getAsJsonObject("variants").entrySet()){
                                CollisionItem.Model itemBlockstate = new CollisionItem.Model();
                                itemBlockstate.variant = pair.getKey();//.isEmpty()? "empty": pair.getKey().replace("=","#");
                                JsonElement value = pair.getValue();
                                if(value.isJsonArray()){ value = value.getAsJsonArray().get(0); }
                                BlockState.Variant variant = NormalGSON.fromJson(value, BlockState.Variant.class);
                                Model model = modelReader.read(new ResourceLocation(variant.model));
                                if(model!=null){
                                    if(model.elements==null){
                                        itemBlockstate.collision = new ArrayList<>();
                                        itemBlockstate.collision.add(CUBE);
                                    }
                                    else{
                                        itemBlockstate.collision = RotateModel.rotate(model, variant.x, variant.y);
                                        itemBlockstate.collision = OptimizeModel.optimize(itemBlockstate.collision,15);

                                    }
                                    item.blockstates.add(itemBlockstate);

                                }
                                else{
                                    System.out.println("Error on"+variant.model);
                                }
                            }
                        }
                    }catch (IOException e){e.printStackTrace();}
                    if(collisionData.containsKey(namespaceIdString)){
                        collisionData.get(namespaceIdString).blockstates = item.blockstates;}
                    else{ collisionData.put(namespaceIdString,item);}
                }

            }
        }
    }

    private void writeCollision(){
        Path path = this._resPath.resolve("./data/"+this._nameSpace+"/collision/");
        path.toFile().mkdirs();
        for(String namespaceIdString:collisionData.keySet()){
            ResourceLocation namespaceId = new ResourceLocation(namespaceIdString);
            Path itemPath = path.resolve(namespaceId.getPath()+".json");
            try(BufferedWriter writer=new BufferedWriter( new FileWriter(itemPath.toFile(), StandardCharsets.UTF_8))){
                String json=NormalGSON.toJson(collisionData.get(namespaceIdString));
                json= json.replaceAll("((?<=[,\\[])\n\s*(?=[-0-9]|\"to\"))|((?<=[0-9])\n\s*(?=]))|((?<=\\{)\n\s*(?=\"from\"))"," ").replaceAll("((?<=([0-9] ]))\n\s*(?=}))"," ");
                writer.write(json);
            }catch (IOException e){ e.printStackTrace();}
        }
    }
    public void create(){
        readBlockStateAndModel();
        writeCollision();
    }

    public void createJson(){
        readBlockStateAndModel();
        this._resPath =this._basePath;
        writeCollision();
    }
}
