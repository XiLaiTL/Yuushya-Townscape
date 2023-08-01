package com.yuushya.datagen;

import com.google.gson.JsonElement;
import com.yuushya.datagen.utils.ResourceLocation;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JarCreator {
    public JarCreator(String nameSpace,Path basePath){
        this._nameSpaceAfter = nameSpace+"_output";
        this._nameSpace = nameSpace;
        this._resPath =  Path.of("../config/com.yuushya/"+nameSpace+"/");
        this._basePath = basePath;
    }
    private final String _nameSpaceAfter;
    private final String _nameSpace;
    private Path _resPath;
    private final Path _basePath;

    private void writeJson(YuushyaDataProvider.ResourceType resource, ResourceLocation resourceLocation, Supplier<JsonElement> getJson) {
        Path path = this._resPath.resolve(resource.name().toLowerCase()+"/"+resourceLocation.getNamespace()+'/'+resourceLocation.getPath());
        path.getParent().toFile().mkdirs();
        try(BufferedWriter writer=new BufferedWriter( new FileWriter(path.toFile()))){
            writer.write(getJson.get().toString());
        }catch (IOException e){ e.printStackTrace();}
    }


    private static void zip(ZipOutputStream out, File file, String base) throws Exception {
        if (file.isDirectory()) {
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
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
            }catch (Exception e){throw e;}
        }
    }

    private void createJar(){
        Path path = Paths.get("./"+this._nameSpaceAfter +".jar");
        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path.toFile()))){
            zip(out, _resPath.toFile(),"");
            out.flush();
        }
        catch (Exception e){}
    }

    private void writeModFile(){
        String fabric_file = """
{
    "schemaVersion": 1,
    "id": "%s",
    "version": "0.0.1",
    "name": "%s",
    "description": "Auto Generation By Yuushya Townscape",
    "environment": "*"
}""".formatted(this._nameSpaceAfter,this._nameSpaceAfter);

        String forge_file = """
modLoader = "lowcodefml"
loaderVersion = "[36,)"
#issueTrackerURL = ""
license="All Rights Reserved"
showAsResourcePack=true
                
[[mods]]
modId = "%s"
version = "0.0.1"
displayName = "%s"
description = "Auto Generation By Yuushya Townscape
""".formatted(this._nameSpaceAfter,this._nameSpaceAfter);
        Path fabric = this._resPath.resolve("fabric.mod.json");
        Path forge = this._resPath.resolve("META-INF/mods.toml");
        forge.getParent().toFile().mkdirs();
        try(FileWriter out = new FileWriter(fabric.toFile())) {
            out.write(fabric_file);
        }catch (IOException e){e.printStackTrace();}
        try(FileWriter out = new FileWriter(forge.toFile())) {
            out.write(forge_file);
        }catch (IOException e){e.printStackTrace();}
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
