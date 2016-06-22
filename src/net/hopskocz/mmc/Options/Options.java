package net.hopskocz.mmc.Options;

import net.hopskocz.mmc.GUI.*;
import net.hopskocz.mmc.Logger.Logger;
import net.hopskocz.mmc.launcher.downloader.DownloadJob;
import net.hopskocz.mmc.launcher.downloader.Downloader;
import net.hopskocz.mmc.launcher.downloader.Parser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.prefs.Preferences;
import java.util.List;

/**
 * Created by hopskocz on 10.06.14.
 */
public class Options {

    private static String gamePath;
    private static boolean loadSnapshots;
    private static boolean loadOlds;
    private static boolean mode;
    private static String currentVersion;
    private static boolean saveLogToFile;
    private static String fakeUUID;

    private static String javaArgs;

    private static Preferences preferences;

    private static ArrayList<String> versionList;
    private static JComboBox<String> comboBox;

    public static void load(Class prefsClass) {
        preferences = Preferences.userNodeForPackage(prefsClass);
        if(System.getProperty("os.name").toLowerCase().contains("windows"))
            gamePath = preferences.get("MainPath", System.getenv("APPDATA")+"/.minemediacenter");
        else
            gamePath = preferences.get("MainPath", System.getProperty("user.home")+"/.minemediacenter");
        loadSnapshots = preferences.getBoolean("LoadSnapshots", false);
        loadOlds = preferences.getBoolean("LoadOlds", false);
        mode = preferences.getBoolean("Mode", false);
        javaArgs = preferences.get("JavaArgs", "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M");
        currentVersion = preferences.get("CurrentVersion", null);
        saveLogToFile = preferences.getBoolean("SaveLogToFile", true);
        fakeUUID = preferences.get("FakeUUID", getRandomUUID());
        //System.out.println(gamePath+" "+loadSnapshots+" "+loadOlds+" "+mode);
        versionList = loadVersions();
        comboBox = new JComboBox<String>();
    }

    private static String getRandomUUID() {
        String uuid = "";

        Random rand = new Random();
        uuid = String.valueOf(rand.nextInt(100000000)+100000000);

        return uuid;
    }

    private static ArrayList<String> loadVersions() {
        ArrayList<String> list = new ArrayList<String>();

        Downloader downloader = new Downloader();
        downloader.addJob(new DownloadJob("http://s3.amazonaws.com/Minecraft.Download/versions/versions.json",Options.getGamePath()+"/versions/versions.json"));
        downloader.run();

        while(!downloader.isFinished()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            list = Parser.parseVersions(Options.getGamePath() + "/versions/versions.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void setVersionList(ArrayList<String> versionList) {
        Options.versionList = versionList;
    }

    public static Preferences getPreferences() {
        return preferences;
    }

    public static NicePanel createInterface() {

        final NicePanel mainPanel = new NicePanel();
        mainPanel.setBackground(new Color(255, 255, 255, 0));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(20,20,20,20);
        c.fill = GridBagConstraints.BOTH;

        AwesomePanel optionPane = new AwesomePanel(false);
        optionPane.setPreferredSize(new Dimension(400,400));
        optionPane.setBgColor(new Color(0,0,0,220));

        JScrollPane scrollPane = new JScrollPane(optionPane);
        scrollPane.setBackground(new Color(255, 255, 255, 0));
        scrollPane.setPreferredSize(new Dimension(400, 400));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        NicePanel center = new NicePanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(new Color(255,255,255,0));

        NicePanel gamePathPanel = new NicePanel();
        gamePathPanel.setBackground(new Color(255, 255, 255, 0));
        gamePathPanel.setBorder(new WhiteTitledBorder("Ścieżka do folderu z grą"));
        final UberTextField gamePathField = new UberTextField(gamePath);
        gamePathField.setBackground(Theme.getLoginFieldBackgroundColor());
        gamePathField.setPreferredSize(new Dimension(240, 30));
        CuteButton gamePathButtonChoose = new CuteButton("Ustaw");
        gamePathButtonChoose.setActionCommand("choose");
        CuteButton gamePathButtonAccept = new CuteButton("OK");
        gamePathButtonAccept.setActionCommand("pathAccept");
        gamePathPanel.add(gamePathField);
        gamePathPanel.add(gamePathButtonChoose);
        gamePathPanel.add(gamePathButtonAccept);

        center.add(gamePathPanel);

        NicePanel placeholder = new NicePanel();
        placeholder.setLayout(new GridLayout(0,2));
        placeholder.setBackground(new Color(255,255,255,0));
        NicePanel modeSettingPanel = new NicePanel();
        modeSettingPanel.setBackground(new Color(255, 255, 255, 0));
        modeSettingPanel.setBorder(new WhiteTitledBorder("Tryb launchera"));
        JRadioButton online = new JRadioButton("Online");
        JRadioButton offline = new JRadioButton("Offline");
        online.setBackground(new Color(255,255,255,0));
        online.setOpaque(false);
        offline.setBackground(new Color(255,255,255,0));
        offline.setSelected(true);
        offline.setOpaque(false);
        modeSettingPanel.add(online);
        modeSettingPanel.add(offline);
        ButtonGroup group = new ButtonGroup();
        group.add(online);
        group.add(offline);

        NicePanel versionsPanel = new NicePanel();
        versionsPanel.setBackground(new Color(255,255,255,0));
        versionsPanel.setBorder(new WhiteTitledBorder("Typy wersji gry"));
        final JCheckBox snapshots = new JCheckBox("Snapshoty");
        snapshots.setBackground(new Color(255, 255, 255, 0));
        snapshots.setActionCommand("snapshots");
        snapshots.setSelected(loadSnapshots);
        snapshots.setOpaque(false);
        final JCheckBox olds = new JCheckBox("Stare wersje");
        olds.setBackground(new Color(255,255,255,0));
        olds.setActionCommand("olds");
        olds.setSelected(loadOlds);
        olds.setOpaque(false);
        versionsPanel.add(snapshots);
        versionsPanel.add(olds);

        placeholder.add(modeSettingPanel);
        placeholder.add(versionsPanel);

        center.add(placeholder);

        NicePanel javaArgsPanel = new NicePanel();
        javaArgsPanel.setBackground(new Color(255, 255, 255, 0));
        javaArgsPanel.setBorder(new WhiteTitledBorder("Dodatkowe parametry"));
        final UberTextField javaArgsField = new UberTextField(javaArgs);
        javaArgsField.setBackground(Theme.getLoginFieldBackgroundColor());
        javaArgsField.setPreferredSize(new Dimension(240, 30));
        CuteButton javaArgsButtonAccept = new CuteButton("OK");
        javaArgsButtonAccept.setActionCommand("argsAccept");
        javaArgsPanel.add(javaArgsField);
        javaArgsPanel.add(javaArgsButtonAccept);

        NicePanel fakeUUIDPanel = new NicePanel();
        fakeUUIDPanel.setBackground(new Color(255, 255, 255, 0));
        fakeUUIDPanel.setBorder(new WhiteTitledBorder("Fake-UUID"));
        final UberTextField fakeUUIDField = new UberTextField(fakeUUID);
        fakeUUIDField.setBackground(Theme.getLoginFieldBackgroundColor());
        fakeUUIDField.setPreferredSize(new Dimension(240, 30));
        CuteButton fakeUUIDButtonAccept = new CuteButton("OK");
        fakeUUIDButtonAccept.setActionCommand("uuidAccept");
        fakeUUIDPanel.add(fakeUUIDField);
        fakeUUIDPanel.add(fakeUUIDButtonAccept);

        NicePanel placeholder2 = new NicePanel();
        placeholder2.setLayout(new GridLayout(0,2));
        placeholder2.setBackground(new Color(255,255,255,0));

        NicePanel versionPanel = new NicePanel();
        versionPanel.setBackground(new Color(255, 255, 255, 0));
        versionPanel.setBorder(new WhiteTitledBorder("Wersja gry"));
        comboBox.setOpaque(false);
        //comboBox.
        reloadVersions();

        if(preferences.get("CurrentVersion",null) != null)
            comboBox.setSelectedItem(currentVersion);
        else
            comboBox.setSelectedIndex(0);
        comboBox.setActionCommand("versionChanged");
        versionPanel.add(comboBox);

        //Log
        NicePanel logPanel = new NicePanel();
        logPanel.setBackground(new Color(255, 255, 255, 0));
        logPanel.setBorder(new WhiteTitledBorder("Logi"));

        final JCheckBox logs = new JCheckBox("Zapis logów do pliku");
        logs.setBackground(new Color(255, 255, 255, 0));
        logs.setActionCommand("logs");
        logs.setSelected(saveLogToFile);
        logs.setOpaque(false);

        logPanel.add(logs);

        placeholder2.add(logPanel);
        placeholder2.add(versionPanel);

        NicePanel themePanel = new NicePanel();
        themePanel.setBackground(new Color(255, 255, 255, 0));
        themePanel.setBorder(new WhiteTitledBorder("Wygląd MMC"));
        JComboBox<File> themes = new JComboBox<File>();
        //ArrayList<File> themeList = Theme.searchForThemes();

        themePanel.add(themes);
        themePanel.add(new JLabel("Soon..."));

        center.add(placeholder2);
        center.add(javaArgsPanel);
        center.add(fakeUUIDPanel);
        center.add(themePanel);

        optionPane.add(center);

        //mainPanel.add(optionPane, c);
        mainPanel.add(scrollPane, c);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getActionCommand().equals("choose")) {
                    JFileChooser chooser = new JFileChooser();
                    if(gamePathField.getText().equals(""))
                        chooser.setCurrentDirectory( new File( System.getProperty("user.dir")) );
                    else
                        chooser.setCurrentDirectory( new File( gamePathField.getText() ) );
                    chooser.setDialogTitle( "Wybierz folder dla Minecraft..." );
                    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
                    // chooser.setAcceptAllFileFilterUsed( false );
                    if( chooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION ) {
                        System.out.println(chooser.getSelectedFile());
                        gamePathField.setText(chooser.getSelectedFile().toString());
                        preferences.put( "MainPath", gamePathField.getText() );
                        JOptionPane.showMessageDialog(mainPanel, "Ustawiono pomyślnie!");
                    }
                }
                if(actionEvent.getActionCommand().equals("pathAccept")) {
                    preferences.put( "MainPath", gamePathField.getText() );
                    JOptionPane.showMessageDialog(mainPanel, "Ustawiono pomyślnie!");
                }
                if(actionEvent.getActionCommand().equals("argsAccept")) {
                    preferences.put("JavaArgs", javaArgsField.getText());
                    JOptionPane.showMessageDialog(mainPanel, "Ustawiono pomyślnie!");
                }
                if(actionEvent.getActionCommand().equals("uuidAccept")) {
                    preferences.put("FakeUUID", fakeUUIDField.getText());
                    JOptionPane.showMessageDialog(mainPanel, "Ustawiono pomyślnie!");
                }
                if(actionEvent.getActionCommand().equals("snapshots")) {
                    loadSnapshots = snapshots.isSelected();
                    preferences.putBoolean("LoadSnapshots", snapshots.isSelected());
                    reloadVersions();
                }
                if(actionEvent.getActionCommand().equals("olds")) {
                    loadOlds = olds.isSelected();
                    preferences.putBoolean("LoadOlds", olds.isSelected());
                    reloadVersions();
                }
                if(actionEvent.getActionCommand().equals("versionChanged")) {
                    if(comboBox.getSelectedItem()!=null) {
                        currentVersion = comboBox.getSelectedItem().toString();
                        preferences.put("CurrentVersion", currentVersion);
                    }
                }
                if(actionEvent.getActionCommand().equals("logs")) {
                    saveLogToFile = logs.isSelected();
                    preferences.putBoolean("SaveLogToFile", logs.isSelected());
                    if(logs.isSelected())
                        Logger.enableSaveToFile();
                    else
                        Logger.disableSaveToFile();
                }
            }
        };

        gamePathButtonChoose.addActionListener(listener);
        gamePathButtonAccept.addActionListener(listener);
        snapshots.addActionListener(listener);
        olds.addActionListener(listener);
        comboBox.addActionListener(listener);
        javaArgsButtonAccept.addActionListener(listener);
        logs.addActionListener(listener);
        fakeUUIDButtonAccept.addActionListener(listener);

        return mainPanel;
    }

    public static void reloadVersions() {
            comboBox.removeAllItems();
            for (String aVersionList : versionList) {
                String[] temp = aVersionList.split(";");
                if ((temp[0].equals("snapshot") && Options.getLoadSnapshots())
                        || ((temp[0].equals("old_beta") || temp[0].equals("old_alpha")) && Options.getLoadOlds())
                        || temp[0].equals("release")) {
                    comboBox.addItem(temp[1]);
                }
            }
    }

    public static void setGamePath(String inGamePath) {
        gamePath = inGamePath;
        preferences.put("MainPath", inGamePath);
    }
    public static String getGamePath() {
        return gamePath;
    }

    public static boolean isSaveLogEnabled() {
        return saveLogToFile;
    }

    public static String getFakeUUID() {
        return fakeUUID;
    }

    public static void setLoadSnapshots(boolean inLoadSnapshots) {
        loadSnapshots = inLoadSnapshots;
        preferences.putBoolean("LoadSnapshots", inLoadSnapshots);
    }
    public static boolean getLoadSnapshots() {
        return loadSnapshots;
    }

    public static void setLoadOlds(boolean inLoadOlds) {
        loadOlds = inLoadOlds;
        preferences.putBoolean("LoadOlds", inLoadOlds);
    }
    public static boolean getLoadOlds() {
        return loadOlds;
    }

    public static void setMode(boolean inMode) {
        mode = inMode;
        preferences.putBoolean("Mode", inMode);
    }
    public static boolean getMode() {
        return mode;
    }

    public static String getCurrentVersion() {
        return currentVersion;
    }

    public static List<String> getJavaArgs() {
        String[] temp = javaArgs.split(" ");
        List<String> list = new ArrayList<String>();
        for(int i = 0; i<temp.length; i++)
            list.add(temp[i]);

        return list;
    }

    static class MyViewPort extends JViewport {
        public MyViewPort() {
            setOpaque(false);
        }
    }

    static class WhiteTitledBorder extends TitledBorder {

        public WhiteTitledBorder(String title) {
            super(title);
            setTitleColor(new Color(225,225,225,255));
        }
    }
}
