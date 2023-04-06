package com.yuushya.collision;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yuushya.collision.data.Model;
import com.yuushya.datagen.utils.ResourceLocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.yuushya.utils.GsonTools.NormalGSON;

public class ModelReader {
    private final Path _basePath;
    public ModelReader(Path basePath){
        this._basePath =basePath;
    }
    public static Map<ResourceLocation,Model> modelMap = new HashMap<>();
    public Model read(ResourceLocation namespaceID){
        if(modelMap.containsKey(namespaceID)) return modelMap.get(namespaceID);
        Path path = this._basePath.resolve("./assets/"+namespaceID.toRelativePath("models")+".json");
        try (JsonReader reader=new JsonReader(new BufferedReader(new FileReader(path.toFile())))){
            Model model = NormalGSON.fromJson(JsonParser.parseReader(reader), Model.class);
            if (model.elements==null&&model.parent!=null) {
                model = read(new ResourceLocation(model.parent));
            }
            modelMap.put(namespaceID,model);
            return model;
        }catch (IOException e){e.printStackTrace();}
        return null;
    }

}
