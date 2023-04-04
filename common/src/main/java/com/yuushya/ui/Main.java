package com.yuushya.ui;

import com.yuushya.collision.CollisionFileCreator;
import com.yuushya.datagen.JarCreator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
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

        JLabel label=new JLabel("Create test");
        label.setBounds(20,20,200,30);
        contentPane.add(label, BorderLayout.SOUTH);
        List<Path> paths=new ArrayList<>();
        JButton button = new JButton("Choose the resource");
        button.setBounds(30,80,200,30);
        button.addActionListener((event)->{
            JFileChooser jFileChooser = new JFileChooser(".");
            jFileChooser.setMultiSelectionEnabled(true);
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if(jFileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
                for (File selectedFile : jFileChooser.getSelectedFiles()) {
                    paths.add(selectedFile.toPath());
                }
            }
        });
        JButton button1 = new JButton("Create the resource");
        button1.setBounds(30,140,200,30);
        button1.addActionListener((event)->{
            for (Path path : paths) {
                String namespace = "yuushya";//path.getFileName().toString()
                JarCreator addon = new JarCreator(namespace, path);
                addon.create();
                CollisionFileCreator collisionFileCreator=new CollisionFileCreator(namespace,path);
                collisionFileCreator.create();
            }
        });
        contentPane.add(button);
        contentPane.add(button1);
        frame.setVisible(true);
    }
}
