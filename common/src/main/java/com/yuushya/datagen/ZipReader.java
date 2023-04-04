package com.yuushya.datagen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ZipReader {
    public static void main(String[] args){

    }
    private final Path _basePath;
    private final Path _resPath;
    private final int _isZip;
    public ZipReader(String nameSpace,Path basePath){
        this._basePath=basePath;
        this._resPath =Path.of("../config/com.yuushya/"+nameSpace+"/");
        this._isZip= Files.isDirectory(basePath) ? 2
                : basePath.toString().matches(".+(\\.zip|\\.jar)") ? 1
                :0;
    }
    public Path filePath(Path realativePath){
        switch (this._isZip){
            case 1:{ //按照zip来读
                try(FileSystem fileSystem = FileSystems.newFileSystem(this._basePath)){
                    Path found= fileSystem.getPath(realativePath.toString());
                    Path output = this._resPath.resolve(realativePath);
                    if(Files.isDirectory(found)){
                        Files.walkFileTree(found,new SimpleFileVisitor<>(){
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
                                Files.copy(file,output.resolve(found.getParent().relativize(file)));
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    }
                    else{
                        Files.copy(found,output, StandardCopyOption.REPLACE_EXISTING);
                    }
                    return output;
                } catch (IOException e) {e.printStackTrace();}
                return this._basePath.getParent().resolve(realativePath);
            }
            case 2:{ //按照文件夹来读
                return this._basePath.resolve(realativePath);
            }
            default : {
                return this._basePath.getParent().resolve(realativePath);
            }
        }
    }
}
