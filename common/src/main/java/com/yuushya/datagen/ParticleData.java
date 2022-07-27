package com.yuushya.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

//could not find the vanilla datagen
public class ParticleData {
    public static JsonElement genParticleDescription(List<String> texturesResourceLocation){
        JsonObject json=new JsonObject();
        JsonArray textures=new JsonArray(); texturesResourceLocation.forEach(textures::add);
        json.add("textures",textures);
        return json;
    }
}
