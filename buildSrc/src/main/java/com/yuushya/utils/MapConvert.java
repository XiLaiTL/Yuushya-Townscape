package com.yuushya.utils;

import at.syntaxerror.syntaxnbt.NBTUtil;
import at.syntaxerror.syntaxnbt.region.Chunk;
import at.syntaxerror.syntaxnbt.region.Region;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.ListTag;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

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
/*
    public static void readFileBySNBT(Path filePath) throws IOException {
        var region =(Region)NBTUtil.deserializeRegion(new RandomAccessFile(filePath.toFile(),"r"));
        //var mcaFile = (MCAFile) MCAUtil.read(filePath.toFile());
        for (int i1 = 0; i1 < 32; i1++)
            for (int i2 = 0; i2 < 32; i2++) { try {//32*32
            var chunk = (Chunk)region.getChunk(i1,i2);
            var data = chunk.getData();
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
    */
    public static void readFileByQNBT(Path filePath) throws IOException {
        var mcaFile = (MCAFile) MCAUtil.read(filePath.toFile());
        for (int i = 0; i < 1024; i++) { try {
            var chunk = mcaFile.getChunk(i);//32*32
            chunk.setStatus("biomes");
            for (var section:chunk) { try{
                for (var blockstate:section.blocksStates()) {try {
                    convertBlockState(blockstate);
                } catch (NullPointerException ignored) {}}
                section.cleanupPaletteAndBlockStates();
            }catch (NullPointerException ignored) {}}
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
        } catch (NullPointerException ignored) {}}
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

    public static void readSave(Path savePath, ConsumerException<Path> readFile) throws Exception {
        var regionPath = savePath.resolve("./region");
        List<String> log = new ArrayList<>();
        try(var filePaths = Files.list(regionPath)){
            var mcaFiles = filePaths.filter(x->x.getFileName().toString().endsWith(".mca")).toList();
            for(var mcaFile: mcaFiles){
                try{
                    readFile.accept(mcaFile);
                }catch (Exception e){
                    log.add(e.getMessage()+"\n"+String.join("\n", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())+"\n"+e.getLocalizedMessage()+"\n");
                }
                System.out.println(mcaFile);
            }
            if(!log.isEmpty()) throw new Exception(String.join("\n",log));
        }
    }
}
