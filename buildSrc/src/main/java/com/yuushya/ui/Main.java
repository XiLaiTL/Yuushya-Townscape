package com.yuushya.ui;

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

        contentPane.add(button0);
        contentPane.add(button);

        frame.setVisible(true);
    }
}
