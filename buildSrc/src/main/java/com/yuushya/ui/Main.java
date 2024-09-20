package com.yuushya.ui;

import com.yuushya.collision.CollisionFileCreator;
import com.yuushya.datagen.JarCreator;
import com.yuushya.utils.ImageSizeReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args){
        String version = "1.16.5";
        String path = ".";
        boolean create = false;
        for (int i = 0; i < args.length; i++){
            if ("-proposal_collision".equals(args[i])){
                Mode.proposalCollision=Mode.registerTableType("collision");
                continue;
            }
            if ("-o".equals(args[i])||"--out".equals(args[i])){
                if (args.length>=i+2){
                    path = args[i+1];
                }
                i++;
                create = true;
                continue;
            }
            if ("-4".equals(args[i])||"--for".equals(args[i])){
                if (args.length>=i+2){
                    version = args[i+1];
                }
                i++;
                create = true;
            }
        }
        if(create){
            System.out.println("Begin Creating");
            Path basePath = Path.of(path);
            System.out.println(basePath.toAbsolutePath().toString());
            JarCreator jarCreator = new JarCreator("yuushya", basePath);
            jarCreator.createJson(version);
            CollisionFileCreator collisionFileCreator=new CollisionFileCreator("yuushya", basePath);
            collisionFileCreator.createJson();
            System.out.println("Create Success");
        }
        else{
            JFrame.setDefaultLookAndFeelDecorated(true);
            JFrame frame=new JFrame("Yuushya Control Panel");
            frame.setSize(300,300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel contentPane=new JPanel();
            contentPane.setLayout(null);
            contentPane.setBorder(new EmptyBorder(5,5,5,5));
            frame.setContentPane(contentPane);

            List<Path> paths=new ArrayList<>();
            JButton button0 = new JButton("存档转换器");
            button0.setBounds(30, 40,200,30);
            button0.addActionListener((event)->{
//            Path self =Paths.get(AssetsAndDataCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20"," ").substring(1));
//            JOptionPane.showMessageDialog(frame,"Load the Dependency. If there was not new window, please reopen! \n正在加载依赖，如果没有新窗口出现，请重新打开软件!");
//            ZipReader zipReader = new ZipReader("lib",self);
//            zipReader.filePath(Path.of("./META-INF/jars"));
                MapConvertor.main(args);
            });
            JButton button = new JButton("资源生成器");
            button.setBounds(30,80,200,30);
            button.addActionListener((event)->{
                AssetsAndDataCreator.main(args);
            });
            JButton button1 = new JButton("图片分辨率检测");
            button1.setBounds(30,120,200,30);
            button1.addActionListener((event)->{
                Mode.imageSize = Mode.registerTableType("image");
                ImageSizeChecker.main(args);
            });

            contentPane.add(button0);
            contentPane.add(button);
            contentPane.add(button1);
            frame.setVisible(true);
        }
    }
}
