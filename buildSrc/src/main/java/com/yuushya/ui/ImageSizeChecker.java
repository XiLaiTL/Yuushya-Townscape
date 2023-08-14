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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.yuushya.utils.FileUtils.copyAll;
import static com.yuushya.utils.FileUtils.deleteAll;

public class ImageSizeChecker {
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


        JLabel label=new JLabel("Yuushya Image Size Checker");
        label.setBounds(20,10,200,30);
        contentPane.add(label, BorderLayout.SOUTH);


        JButton button = new JButton("Choose the pack");
        button.setBounds(30,80,200,30);
        button.addActionListener((event)->{
            JFileChooser jFileChooser = new JFileChooser(".");
            jFileChooser.setMultiSelectionEnabled(true);
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jFileChooser.setFileFilter(new FileNameExtensionFilter("resourcepacks or mod jars","zip","jar"));
            if(jFileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
                for (File selectedFile : jFileChooser.getSelectedFiles()) {
                    paths.add(selectedFile.toPath());
                }
            }
        });


        Path self = Paths.get(ImageSizeChecker.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20"," ").substring(1));
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

                    if(Mode.imageSize!=null){
                        ImageSizeReader imageSizeReader = new ImageSizeReader(pathTemp);
                        imageSizeReader.readAllPng();
                    }

                    YuushyaLog.print();
                    JOptionPane.showMessageDialog(frame,"Success!");
                    deleteAll(pathTemp);
                }
            }
            catch (Exception e1){e1.printStackTrace();YuushyaLog.error(e1);}

        });


        contentPane.add(button);
        contentPane.add(button1);


        frame.setVisible(true);
    }








}
