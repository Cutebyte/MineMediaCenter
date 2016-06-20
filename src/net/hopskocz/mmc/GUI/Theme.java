package net.hopskocz.mmc.GUI;

import net.hopskocz.mmc.Logger.Logger;
import net.hopskocz.mmc.Options.Options;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * MineMediaCenter
 */
public class Theme {
    //theme path
    //private String themePath; //todo: add loading themes
    // + main colors
    //colors
    private static Color bottomBarColor; //bgColor of bottom bar
    private static Color upperBarColor; //bgColor of upper bar :NoShitSherlock:
    private static Color backgroundColor; //main window background / useful when image not loaded
    //images
    private static String bgImagePath; //animation or static image
    //sizes
    private static int bottomBarSize;
    private static int upperBarSize;
    // - main colors
    // + login button
    private static Color loginButtonColor;
    private static Color loginButtonHover;
    private static Color loginButtonClicked;
    private static String loginButtonIconPath;
    private static Dimension loginButtonSize;
    // - login button
    // + login box
    private static Dimension loginBoxSize;
    private static Color loginBoxPanelBgColor;
    private static Dimension loginFieldSize;
    private static Dimension passwordFieldSize;
    private static Dimension loginBoxButtonSize;
    private static Dimension loginBoxButtonPanelSize;

    private static Color loginFieldBackgroundColor;
    private static Color passwordFieldBackgroundColor;
    // - login box

    //theme list
    //private static ArrayList<File> themes;

    public static void init() { //default colors and image
        defaults();
    }

    private static void defaults() {
        bottomBarColor = new Color(0,0,0,122);
        upperBarColor = new Color(0,0,0,122);
        backgroundColor = new Color(0,129,255,255);
        bgImagePath = "/net/hopskocz/mmc/res/";
        bottomBarSize = 40;
        upperBarSize = 40;

        loginButtonColor = new Color(255,255,255,0);
        loginButtonHover = new Color(255,255,255,30);
        loginButtonClicked = new Color(0,0,0,50);
        loginButtonIconPath = "/net/hopskocz/mmc/res/loginBtnBg.png";
        loginButtonSize = new Dimension(32,32);

        loginBoxSize = new Dimension(300,140);
        loginBoxPanelBgColor = new Color(0,0,0,122);
        loginFieldSize = new Dimension(270,30);
        passwordFieldSize = new Dimension(270,30);
        loginBoxButtonPanelSize = new Dimension(270,36);
        loginBoxButtonSize = new Dimension(100,30);

        loginFieldBackgroundColor = new Color(0, 0, 0, 50);
        passwordFieldBackgroundColor = new Color(0, 0, 0, 50);

        UIDefaults uiDefaults = UIManager.getDefaults();
        Color bgColor = new ColorUIResource(new Color(225,225,225,255));
        for (Enumeration e = uiDefaults.keys(); e.hasMoreElements(); ) {
            Object obj = e.nextElement();
            if (obj instanceof String) {
                if (((String) obj).matches(".*\\.foreground$")
                        && uiDefaults.get(obj) instanceof Color) {
                    uiDefaults.put(obj, bgColor);
                }
            }
        }
    }

    public static ArrayList<File> searchForThemes() {
        ArrayList<File> themes = new ArrayList<File>();
        File folder = new File(Options.getGamePath() + "/Themes/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".txt")) {
                    System.out.println("[ThemeLoader]: Theme found: " + listOfFiles[i].getName());
                    themes.add(listOfFiles[i]);
                }
            }
        }
        return themes;
    }

    public static void loadTheme() {

    }

    public static Color getBottomBarColor() { return bottomBarColor; }
    public static Color getUpperBarColor() { return upperBarColor; }
    public static Color getBackgroundColor() { return backgroundColor; }
    public static String getBgImagePath() { return bgImagePath; }
    public static int getBottomBarSize() { return bottomBarSize; }
    public static int getUpperBarSize() { return upperBarSize; }

    public static Color getLoginButtonColor() { return loginButtonColor; }
    public static Color getLoginButtonHover() { return loginButtonHover; }
    public static Color getLoginButtonClicked() { return loginButtonClicked; }
    public static String getLoginButtonIconPath() { return loginButtonIconPath; }
    public static Dimension getLoginButtonSize() { return loginButtonSize; }

    public static Dimension getLoginBoxSize() { return loginBoxSize; }
    public static Color getLoginBoxPanelBgColor() { return loginBoxPanelBgColor; }
    public static Dimension getLoginFieldSize() { return loginFieldSize; }
    public static Dimension getPasswordFieldSize() { return passwordFieldSize; }
    public static Dimension getLoginBoxButtonPanelSize() { return loginBoxButtonPanelSize; }
    public static Dimension getLoginBoxButtonSize() { return loginBoxButtonSize; }
    public static Color getLoginFieldBackgroundColor() { return loginFieldBackgroundColor; }
    public static Color getPasswordFieldBackgroundColor() { return passwordFieldBackgroundColor; }
}
