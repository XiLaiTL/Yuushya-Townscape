package com.yuushya.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.ListTag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MapConvert {
    private static final Map<String,String> nameMap = new HashMap<>();
    public static void readMap() throws IOException {

        try(InputStream inputStream = MapConvert.class.getResourceAsStream("/data/yuushya/mapping/name_mapping.json")){
            if(inputStream!=null){
                var jsonElement =(JsonElement) JsonParser.parseReader(new BufferedReader(new InputStreamReader(inputStream)));
                var jsonArray = jsonElement.getAsJsonArray();
                var from = "yuushya1.10.2";
                var to = "yuushya2.0.0";
                for(var obj : jsonArray){
                    var jsonObject = obj.getAsJsonObject();
                    if(jsonObject.has(from)&&jsonObject.has(to)){
                        var fromName = "yuushya:"+jsonObject.get(from).getAsString();
                        var toName = jsonObject.get(to).getAsString();
                        if(!toName.contains("minecraft:")) toName = "yuushya:"+toName;
                        nameMap.put(fromName,toName);
                    }
                }

            }
        }

//        for(var keyAndValue: nameMap.entrySet()){
//            System.out.println(keyAndValue.getKey()+" -> "+keyAndValue.getValue());
//        }
    }
    public static void readFile(Path filePath) throws IOException {
        var mcaFile = (MCAFile) MCAUtil.read(filePath.toFile());
        for (int i = 0; i < 1024; i++) { try {
            var chunk = mcaFile.getChunk(i);//32*32
            for (int j = 0; j < 16; j++) { try{
                var section = chunk.getSection(j);
                for (int x = 0; x < 16; x++) for (int y = 0; y < 16; y++) for (int z = 0; z < 16; z++) {try {
                    var blockstate = (CompoundTag) section.getBlockStateAt(x, y, z);
                    section.setBlockStateAt(x, y, z, convertBlockState(blockstate), false);
                } catch (NullPointerException ne) {}}
            }catch (NullPointerException ne) {}}
            var tileEntities = (ListTag<CompoundTag>) chunk.getTileEntities();
            for(var tileEntity:tileEntities){
                var id = tileEntity.getString("id");
                if(id.equals("yuushya:mixedblockentity")||id.equals("yuushya:showblockentity")){
                    if(id.equals("yuushya:mixedblockentity")){
                        tileEntity.putString("id","yuushya:showblockentity");
                    }
                    var showPos = tileEntity.getListTag("ShowPos");
                    var showRotation = tileEntity.getListTag("ShowRotation").asFloatTagList();
                    var blocks = tileEntity.getListTag("Blocks");
                    if(!(showPos.size()<0||showRotation.size()<0||blocks.size()<=0)){
                        var firstBlock =  (CompoundTag) blocks.get(0);
                        firstBlock.put("ShowPos",showPos);
                        firstBlock.put("ShowRotation",showRotation);
//                        var showRotationNew = new ListTag<FloatTag>(FloatTag.class);
//                        showRotationNew.add(showRotation.get(2));showRotationNew.add(showRotation.get(1));showRotationNew.add(showRotation.get(0));
//                        firstBlock.put("ShowRotation",showRotationNew);
                        for(var blockTag:blocks){
                            var block = (CompoundTag) blockTag;
                            var blockstate = block.getCompoundTag("BlockState");
                            block.put("BlockState",convertBlockState(blockstate));
                            block.putByte("isShown",(byte)1 );
                            var showScales = new ListTag<FloatTag>(FloatTag.class);
                            showScales.addFloat(1f);showScales.addFloat(1f);showScales.addFloat(1f);
                            block.put("ShowScales",showScales);
                        }
                    }
                }
            }
            chunk.setTileEntities(tileEntities);
        } catch (NullPointerException ne) {}}
        MCAUtil.write(mcaFile, filePath.toFile());

    }

    public static CompoundTag convertBlockState(CompoundTag blockstate){
        var blockName = blockstate.getString("Name");
        if(blockName.contains("yuushya")) {
            if(!nameMap.containsKey(blockName)){
                System.out.println("Could not find "+blockName);
                return blockstate;
            }
            blockstate.putString("Name",nameMap.get(blockName));
            if(blockstate.containsKey("Properties")){
                var properties = blockstate.getCompoundTag("Properties");
                if(properties.containsKey("facing")){
                    var facing = properties.getString("facing");
                    var convertFacing = switch (facing){
                        case "north"->"south";
                        case "south"->"north";
                        case "east"->"west";
                        case "west"->"east";
                        default -> "south";
                    };
                    properties.putString("facing",convertFacing);
                }
                if(properties.containsKey("powered")){
                    var powered = properties.getBoolean("powered");
                    if(powered){
                        properties.putInt("form",1);
                    }
                }
                blockstate.put("Properties",properties);
            }
        }

        return blockstate;
    }

    public static void readSave(Path savePath) throws IOException {
        var regionPath = savePath.resolve("./region");
        var exp = new IOException();
        try(var filePaths = Files.list(regionPath)){
            var mcaFiles = filePaths.filter(x->x.getFileName().toString().endsWith(".mca")).toList();
            for(var mcaFile: mcaFiles){
                try{
                    readFile(mcaFile);
                } catch (IOException e){ e.printStackTrace();}
            }
        }
    }
}
