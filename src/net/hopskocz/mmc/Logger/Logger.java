package net.hopskocz.mmc.Logger;

import net.hopskocz.mmc.GUI.AwesomePanel;
import net.hopskocz.mmc.GUI.CuteButton;
import net.hopskocz.mmc.GUI.MMCFrame;
import net.hopskocz.mmc.GUI.NicePanel;
import net.hopskocz.mmc.Options.Options;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tomek on 2014-09-05.
 */
public class Logger {
    private static JTextArea jTextArea;
    private static PrintWriter out;

    public static void error(String message) {
        print("[ERROR]: "+message);
    }
    public static void warning(String message) {
        print("[WARNING]: "+message);
    }
    public static void info(String message) {
        print("[INFO]: "+message);
    }
    public static void print(String message) {
        System.out.println(message);
        jTextArea.append(message+"\n");
        if(Options.isSaveLogEnabled()) {
            out.println(message);
        }
    }

    public static NicePanel createInterface() {
        NicePanel mainPanel = new NicePanel();
        mainPanel.setBackground(new Color(255, 255, 255, 0));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(20,20,20,20);
        c.fill = GridBagConstraints.BOTH;

        NicePanel container = new NicePanel();
        container.setLayout(new BorderLayout());
        container.setBackground(new Color(255,255,255,0));

        AwesomePanel loggerPane = new AwesomePanel(false);
        loggerPane.setBgColor(new Color(0,0,0,220));
        loggerPane.setPreferredSize(new Dimension(400,400));
        loggerPane.setLayout(new GridBagLayout());

        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setForeground(new Color(225,225,225));
        jTextArea.setOpaque(false);

        DefaultCaret caret = (DefaultCaret)jTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane areaScrollPane = new JScrollPane(jTextArea);
        areaScrollPane.setViewportView(jTextArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.getViewport().setOpaque(false);
        areaScrollPane.setOpaque(false);

        NicePanel buttonPanel = new NicePanel();
        buttonPanel.setLayout(new FlowLayout());
        CuteButton openLog = new CuteButton("Otwórz inny plik logu");
        CuteButton lastLog = new CuteButton("Otwórz aktualny plik logu");

        container.add(areaScrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        loggerPane.add(container, c);
        mainPanel.add(loggerPane, c);

        if(Options.isSaveLogEnabled())
            enableSaveToFile();

        return mainPanel;
    }

    public static void enableSaveToFile() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH-mm-ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        File log = new File(Options.getGamePath()+"/logs/MMC/"+dateFormat.format(date)+".txt");
        log.getParentFile().mkdirs();
        if(out == null)
            try {
                out = new PrintWriter(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    public static void disableSaveToFile() {
        out.close();
        out = null;
        System.out.println("File stream closed successfully!");
    }
}
