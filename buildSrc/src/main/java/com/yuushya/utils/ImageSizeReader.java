package com.yuushya.utils;

import com.yuushya.ui.Main;
import com.yuushya.ui.Mode;
import com.yuushya.ui.YuushyaLog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ImageSizeReader {
    private final Path _basePath;

    public ImageSizeReader(Path basePath) {
        _basePath = basePath;
    }

    public void readAllPng(){
        try {
            Files.walkFileTree(_basePath,new SimpleFileVisitor<>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().endsWith(".png")){
                        BufferedImage sourceImg = ImageIO.read(Files.newInputStream(file));
                        int width = sourceImg.getWidth();
                        int height = sourceImg.getHeight();
                        String name = _basePath.relativize(file).toString();
                        if(width%2!=0 || height%2!=0)
                            name = "Error::"+ name;
                        Mode.add("image", name+"\t" +width+"\t"+height);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {e.printStackTrace();YuushyaLog.error(e);}

    }
}
