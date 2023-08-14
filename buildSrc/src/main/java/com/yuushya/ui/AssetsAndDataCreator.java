package com.yuushya.ui;

import com.google.gson.stream.JsonReader;
import com.yuushya.collision.CollisionFileCreator;
import com.yuushya.datagen.ConfigReader;
import com.yuushya.datagen.JarCreator;
import com.yuushya.datagen.ZipReader;
import com.yuushya.datagen.data.PackData;
import com.yuushya.utils.GsonTools;
import com.yuushya.utils.ImageSizeReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static com.yuushya.utils.FileUtils.copyAll;
import static com.yuushya.utils.FileUtils.deleteAll;

public class AssetsAndDataCreator {
    static List<Path> paths=new ArrayList<>();
    public static void main(String[] args){
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame=new JFrame("Yuushya Control Panel");
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPane=new JPanel();
        contentPane.setLayout(null);
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        frame.setContentPane(contentPane);
//        JOptionPane.showMessageDialog(null,"Hello");

        JLabel label=new JLabel("Yuushya Creator");
        label.setBounds(20,10,200,30);
        contentPane.add(label, BorderLayout.SOUTH);

//        JButton button0 = new JButton("Choose the minecraft jar");
//        button0.setBounds(30, 40,200,30);
//        button0.setToolTipText("under version folder");
//        button0.addActionListener((event)->{
//            JFileChooser jFileChooser = new JFileChooser("../");
//            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//            jFileChooser.setFileFilter(new FileNameExtensionFilter("minecraft jar","jar"));
//            if(jFileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
//                File selectedFile = jFileChooser.getSelectedFile();
//                ZipReader zipReader = new ZipReader("yuushya",selectedFile.toPath());
//                zipReader.filePath(Path.of("./assets/minecraft/models/"));
//            }
//        });

        JButton button = new JButton("Choose the addon");
        button.setBounds(30,80,200,30);
        button.addActionListener((event)->{
            JFileChooser jFileChooser = new JFileChooser(".");
            jFileChooser.setMultiSelectionEnabled(true);
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jFileChooser.setFileFilter(new FileNameExtensionFilter("yuushya addon resourcepacks or mod jars","zip","jar"));
            if(jFileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
                for (File selectedFile : jFileChooser.getSelectedFiles()) {
                    paths.add(selectedFile.toPath());
                }
            }
        });
//        JButton button3 = new JButton("Choose Yuushya Mod");
//        button3.setBounds(30,120,200,30);
//        button3.setToolTipText("yuushya-townscape mod");
//        button3.addActionListener((event)->{
//            JFileChooser jFileChooser = new JFileChooser(".");
//            jFileChooser.setMultiSelectionEnabled(true);
//            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//            jFileChooser.setFileFilter(new FileNameExtensionFilter("yuushya-townscape mod","zip","jar"));
//            if(jFileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
//                for (File selectedFile : jFileChooser.getSelectedFiles()) {
//                    paths.add(selectedFile.toPath());
//                }
//            }
////            Path self =Paths.get(AssetsAndDataCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20"," ").substring(1));
////            paths.add(self);
//        });

        JCheckBox chkbox1=new JCheckBox("Create the Resource And Data", true);
        chkbox1.setBounds(30,120,200,30);
        JCheckBox chkbox2=new JCheckBox("Create the Collision", true);
        chkbox2.setBounds(30,160,200,30);
        Path self = Paths.get(AssetsAndDataCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20"," ").substring(1));
        Path pathTemp = self.getParent().resolve("temp");

        JButton button1 = new JButton("Create");
        button1.setBounds(30,200,200,30);
        button1.addActionListener((event)-> {
            try {
                for (Path path : paths) {
                    if(Files.isDirectory(path)){
                        copyAll(path,pathTemp);
                    }
                    else{
                        ZipReader zipReader = new ZipReader(path,pathTemp);
                        zipReader.unzip();
                    }

                    PackData packData;
                    try (JsonReader reader = new JsonReader(Files.newBufferedReader(pathTemp.resolve("./pack.mcmeta")))){
                        packData = GsonTools.NormalGSON.fromJson(reader,PackData.class);
                    }
                    catch (IOException e){packData = new PackData();YuushyaLog.error(e);}
                    packData.checkVaild(path.getFileName().toString().replace(".zip","").replace(".jar","").toLowerCase().replace("-","_".replace(" ","_").replace(".","_")));
                    JarCreator jarCreator = new JarCreator(packData,pathTemp,pathTemp);
                    if (chkbox1.isSelected()) jarCreator.createJson();
                    CollisionFileCreator collisionFileCreator = new CollisionFileCreator(pathTemp,pathTemp);
                    if (chkbox2.isSelected()) collisionFileCreator.createJson();
                    jarCreator.writeModFile();
                    jarCreator.createJar();
                    YuushyaLog.print();
                    JOptionPane.showMessageDialog(frame,"Success!");
                    deleteAll(pathTemp);
                }
            }
            catch (Exception e1){e1.printStackTrace();YuushyaLog.error(e1);}

        });

//        JButton button2 = new JButton("Create the collision");
//        button2.setBounds(30,200,200,30);
//        button2.addActionListener((event)-> createCollision());

        //contentPane.add(button0);
        contentPane.add(button);
        //contentPane.add(button3);
        contentPane.add(button1);
        //contentPane.add(button2);
        contentPane.add(chkbox1);
        contentPane.add(chkbox2);
        frame.setVisible(true);
    }




    public static void createResource(){
        for (Path path : paths) {
            String namespace = "yuushya";//path.getFileName().toString()
            if(Files.isDirectory(path)){
                JarCreator addon = new JarCreator(namespace, path);
                addon.create();
            }
            else{
                Path copyPath = Path.of("../config/com.yuushya/"+namespace+"/");
                ZipReader zipReader = new ZipReader(namespace,path);
                zipReader.filePath(Path.of("./data/"+namespace + "/register/"));
                JarCreator addon = new JarCreator(namespace, copyPath);
                addon.create();
            }
        }
    }

    public static void createCollision(){
        for (Path path : paths) {
            String namespace = "yuushya";//path.getFileName().toString()
            if(Files.isDirectory(path)){
                ConfigReader configReader = new ConfigReader();
                configReader.readRegistryConfig(path.resolve("./data/"+namespace+"/register/"));
                configReader.generateRegistries();
                CollisionFileCreator collisionFileCreator=new CollisionFileCreator(namespace,path);
                collisionFileCreator.create();
            }
            else{
                Path copyPath = Path.of("../config/com.yuushya/"+namespace+"/");
                ZipReader zipReader = new ZipReader(namespace,path);
                zipReader.filePath(Path.of("./data/"+namespace + "/register/"));
                zipReader.filePath(Path.of("./assets/"+namespace + "/blockstates/"));
                zipReader.filePath(Path.of("./assets/"+namespace + "/models/"));
                ConfigReader configReader = new ConfigReader();
                configReader.readRegistryConfig(copyPath.resolve("./data/"+namespace+"/register/"));
                configReader.generateRegistries();
                CollisionFileCreator collisionFileCreator=new CollisionFileCreator(namespace,copyPath);
                collisionFileCreator.create();
            }
        }
    }
}
