package com.yuushya.ui;

import com.yuushya.utils.MapConvert;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.yuushya.utils.MapConvert.readMap;
import static com.yuushya.utils.MapConvert.readSave;

public class MapConvertor {

    public static void main(String[] args) {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame=new JFrame("Yuushya Control Panel");
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane=new JPanel();
        contentPane.setLayout(null);
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        frame.setContentPane(contentPane);

        try{
            MapConvert.readMap();}catch (IOException e) {
            JOptionPane.showMessageDialog(frame,e.getMessage());
        }

        JLabel label=new JLabel("");
        label.setBounds(20,70,200,90);
        contentPane.add(label, BorderLayout.SOUTH);

        List<Path> paths=new ArrayList<>();
        JButton button0 = new JButton("打开存档\n Choose the minecraft saves");
        button0.setBounds(30, 40,200,30);
        button0.addActionListener((event)->{
            JFileChooser jFileChooser = new JFileChooser("../saves/");
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(jFileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
                File selectedFile = jFileChooser.getSelectedFile();
                paths.add(selectedFile.toPath());
                label.setText("<html>"+selectedFile.getAbsolutePath()+"</html>");
            }
        });

        JButton button1 = new JButton("进行转换\n Convert");
        button1.setBounds(30, 160,200,30);
        button1.addActionListener((event)->{
            try{
                for(var path:paths){
                    MapConvert.readSave(path);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame,e.getStackTrace());
            }
            JOptionPane.showMessageDialog(frame,"Success!");
        });
        contentPane.add(button0);
        contentPane.add(button1);
        frame.setVisible(true);

    }
}

