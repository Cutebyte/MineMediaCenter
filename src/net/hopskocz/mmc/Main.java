package net.hopskocz.mmc;

import net.hopskocz.mmc.GUI.Theme;
import net.hopskocz.mmc.Logger.Logger;
import net.hopskocz.mmc.launcher.downloader.DownloadJob;
import net.hopskocz.mmc.launcher.downloader.Downloader;
import net.hopskocz.mmc.launcher.downloader.Parser;
import net.hopskocz.mmc.GUI.MMCFrame;
import net.hopskocz.mmc.Options.Options;
import net.hopskocz.mmc.Plugins.Plugin;
import net.hopskocz.mmc.Plugins.PluginLoader;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by hopskocz on 06.04.14.
 */
public class Main {
    private static String version = "0.1.0";
    private static MMCFrame frame;
    private static boolean done = false;

    public static void main( String[] args ) {

        Options.load(Main.class);
        Theme.init();

        frame = new MMCFrame(version);

        //Options - loading options and everything
        frame.addNewTab("Opcje", Options.createInterface());
        frame.addNewTab("Logi", Logger.createInterface());

        Logger.info("MMC started");

        frame.addWindowListener( new WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent windowEvent) {
                if(!done) {
                    frame.setBorderVerticalSize(frame.getHeight()-frame.getRootPane().getHeight());
                    frame.setBorderHorizontalSize(frame.getWidth()-frame.getRootPane().getWidth());
                    frame.validate();
                    done = true;
                }
                super.windowActivated(windowEvent);
            }
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Logger.info("MMC died! ^^");
                Logger.disableSaveToFile();
                System.exit(0);
            }
        });

        Logger.info("[PluginLoader]: started...");

        //plugin loading - NEW way - more shit coming from there
        PluginLoader pluginLoader = new PluginLoader(Main.class.getClassLoader());
        ArrayList<Plugin> pluginList = new ArrayList<Plugin>();
        try {
            pluginLoader.loadPluginsMoarAwesome(pluginList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        Logger.info("[PluginLoader]: Attempting to load plugins...");

        //load interface
        for(Iterator<Plugin> itr = pluginList.iterator(); itr.hasNext(); ) {
            Plugin tempPlugin = itr.next();
            if(tempPlugin.haveInterface()) {
                try {
                    frame.addNewTab(tempPlugin.getName(), (JPanel) tempPlugin.getMethod("createInterface").invoke(tempPlugin.getObject()));
                    Logger.info("[PluginLoader]: Successfully loaded \""+tempPlugin.getName()+"\"");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }

        Logger.info("[PluginLoader]: Finished");

        frame.setVisible(true);
    }
}