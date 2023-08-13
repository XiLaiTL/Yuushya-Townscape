package com.yuushya.datagen;

import com.google.gson.JsonElement;
import com.yuushya.datagen.data.PackData;
import com.yuushya.datagen.utils.ResourceLocation;
import com.yuushya.ui.YuushyaLog;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.yuushya.datagen.utils.Utils.MOD_ID;

public class JarCreator {
    public JarCreator(String nameSpaceYuushya, Path basePath){
        this._nameSpaceAfter = nameSpaceYuushya +"_output";
        this._nameSpace = nameSpaceYuushya;
        this._resPath =  Path.of("../config/com.yuushya/"+ nameSpaceYuushya +"/");
        this._basePath = basePath;
    }
    //resPath 是json生成的地址
    public JarCreator(PackData packData,Path basePath,Path resPath){
        this._nameSpaceAfter = packData.mod.name;
        this._packData = packData;
        this._nameSpace = MOD_ID;
        this._resPath = resPath;
        this._basePath = basePath;
    }
    private final String _nameSpaceAfter;
    private final String _nameSpace;
    private Path _resPath;
    private PackData _packData;
    private final Path _basePath;

    private void writeJson(YuushyaDataProvider.ResourceType resource, ResourceLocation resourceLocation, Supplier<JsonElement> getJson) {
        Path path = this._resPath.resolve(resource.name().toLowerCase()+"/"+resourceLocation.getNamespace()+'/'+resourceLocation.getPath());
        path.getParent().toFile().mkdirs();
        try(BufferedWriter writer=new BufferedWriter( new FileWriter(path.toFile()))){
            JsonElement element = null;
            try {
                element = getJson.get();
            }catch (Exception e){
                System.out.println(resource +" "+resourceLocation);
                e.printStackTrace();
                YuushyaLog.error(e);
            }
            if(element !=null) writer.write(element.toString());
        }catch (IOException e){ e.printStackTrace();YuushyaLog.error(e);}
    }


    private static void zip(ZipOutputStream out, File file, String base) throws Exception {
        if (file.isDirectory()) {
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.isEmpty() ? "" : base + "/";
            for (File file1 : file.listFiles()) {
                zip(out, file1, base + file1.getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            try(FileInputStream in = new FileInputStream(file)){
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
            }catch (Exception e){e.printStackTrace(); YuushyaLog.error(e);}
        }
    }

    public void createJar(){
        Path path = Paths.get("./"+this._nameSpaceAfter +"."+MOD_ID+".jar");
        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path.toFile()))){
            zip(out, _resPath.toFile(),"");
            out.flush();
        }
        catch (Exception e){e.printStackTrace(); YuushyaLog.error(e);}
    }

    public void writeModFile(){
        String fabric_file = MessageFormat.format("""
'{'
    "schemaVersion": 1,
    "id": "{0}",
    "version": "{1}",
    "name": "{0}",
    "description": "{2}",
    "environment": "*",
    "icon": "pack.png",
    "depends": '{'
        "yuushya":">=2.0.0"
    '}'
'}'""",_packData.mod.name,_packData.mod.version,_packData.pack.description);

        String forge_file = MessageFormat.format( """
modLoader = "lowcodefml"
loaderVersion = "[36,)"
#issueTrackerURL = ""
license="All Rights Reserved"
showAsResourcePack=true
                
[[mods]]
modId = "{0}"
version = "{1}"
displayName = "{0}"
description = "{2}"
logoFile = "pack.png"

[[dependencies.{0}]]
modId = "yuushya"
mandatory = true
versionRange = "[2.0.0,)"
ordering = "AFTER"
side = "BOTH"
""",_packData.mod.name,_packData.mod.version,_packData.pack.description);
        Path fabric = this._resPath.resolve("fabric.mod.json");
        Path forge = this._resPath.resolve("META-INF/mods.toml");
        forge.getParent().toFile().mkdirs();
        try(FileWriter out = new FileWriter(fabric.toFile())) {
            out.write(fabric_file);
        }catch (IOException e){e.printStackTrace(); YuushyaLog.error(e);}
        try(FileWriter out = new FileWriter(forge.toFile())) {
            out.write(forge_file);
        }catch (IOException e){e.printStackTrace(); YuushyaLog.error(e);}
    }

    public void createJson(){
        this._resPath = this._basePath;
        ConfigReader configReader = new ConfigReader();
        configReader.readRegistryConfig(this._basePath.resolve("./data/"+this._nameSpace +"/register/"));
        configReader.generateRegistries();
        YuushyaDataProvider yuushyaDataProvider = new YuushyaDataProvider();
        for(var dataType:YuushyaDataProvider.DataType.values() ){
            yuushyaDataProvider.type(dataType).forEach((key,value)->{writeJson(dataType.resource,key,value);});
        }
    }

    public void create(){
        ConfigReader configReader = new ConfigReader();
        configReader.readRegistryConfig(this._basePath.resolve("./data/"+this._nameSpace +"/register/"));
        configReader.generateRegistries();
        configReader.writeRegistryConfig();
        YuushyaDataProvider yuushyaDataProvider = new YuushyaDataProvider();
        for(var dataType:YuushyaDataProvider.DataType.values() ){
            yuushyaDataProvider.type(dataType).forEach((key,value)->{writeJson(dataType.resource,key,value);});
        }
        writeModFile();
        createJar();
    }



}
