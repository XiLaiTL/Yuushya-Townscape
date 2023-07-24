package com.yuushya.ui;

import com.yuushya.collision.CollisionFileCreator;
import com.yuushya.datagen.ConfigReader;
import com.yuushya.datagen.JarCreator;
import com.yuushya.datagen.ZipReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AssetsAndDataCreator {
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
        List<Path> paths=new ArrayList<>();
        JButton button0 = new JButton("Choose the minecraft jar");
        button0.setBounds(30, 40,200,30);
        button0.addActionListener((event)->{
            JFileChooser jFileChooser = new JFileChooser("../");
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.setFileFilter(new FileNameExtensionFilter("minecraft jar","jar"));
            if(jFileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
                File selectedFile = jFileChooser.getSelectedFile();
                ZipReader zipReader = new ZipReader("yuushya",selectedFile.toPath());
                zipReader.filePath(Path.of("./assets/minecraft/models/"));
            }
        });
        JButton button = new JButton("Choose the addon");
        button.setBounds(30,80,170,30);
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
        JButton button3 = new JButton("Self");
        button3.setBounds(200,80,30,30);
        button3.setToolTipText("This jar itself as addon");
        button3.addActionListener((event)->{
            Path self =Paths.get(AssetsAndDataCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20"," ").substring(1));
            paths.add(self);
        });
        JButton button1 = new JButton("Create the resource");
        button1.setBounds(30,140,200,30);
        button1.addActionListener((event)->{
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
        });
        JButton button2 = new JButton("Create the collision");
        button2.setBounds(30,180,200,30);
        button2.addActionListener((event)->{
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
        });
        contentPane.add(button0);
        contentPane.add(button);
        contentPane.add(button3);
        contentPane.add(button1);
        contentPane.add(button2);
        frame.setVisible(true);
    }
}
