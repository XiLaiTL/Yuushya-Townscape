package com.yuushya.datagen;

import com.yuushya.ui.YuushyaLog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ZipReader {

    private final Path _basePath;
    private final Path _resPath;

    public static int isZip(Path basePath){
        return Files.isDirectory(basePath) ? 2
                : basePath.toString().matches(".+(\\.zip|\\.jar)") ? 1
                :0;
    }
    public ZipReader(String nameSpace,Path basePath){
        this._basePath=basePath;
        this._resPath =Path.of("../config/com.yuushya/"+nameSpace+"/");
    }
    public ZipReader(Path basePath,Path resPath){
        this._basePath=basePath;
        this._resPath =resPath;
    }

    public void unzip(){
        if(isZip(_basePath)==1){
            try(FileSystem fileSystem = FileSystems.newFileSystem(this._basePath)){
                Files.walkFileTree(fileSystem.getPath("/"), new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Path pathNew = ZipReader.this._resPath.resolve("./"+file.toString());
                        pathNew.getParent().toFile().mkdirs();
                        Files.copy(file,pathNew,StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {e.printStackTrace(); YuushyaLog.add(e); }

        }
    }

    public Path filePath(Path realativePath){
        switch (isZip(_basePath)){
            case 1:{ //按照zip来读
                try(FileSystem fileSystem = FileSystems.newFileSystem(this._basePath)){
                    Path found= fileSystem.getPath(realativePath.toString());
                    Path output = this._resPath.resolve(realativePath);
                    if(Files.isDirectory(found)){
                        Files.walkFileTree(found,new SimpleFileVisitor<>(){
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
                                Path pathNew = output.resolve(found.relativize(file).normalize().toString());
                                pathNew.getParent().toFile().mkdirs();
                                Files.copy(file,pathNew,StandardCopyOption.REPLACE_EXISTING);
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    }
                    else{
                        Files.copy(found,output, StandardCopyOption.REPLACE_EXISTING);
                    }
                    return output;
                } catch (IOException e) {e.printStackTrace(); YuushyaLog.add(e); }
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
