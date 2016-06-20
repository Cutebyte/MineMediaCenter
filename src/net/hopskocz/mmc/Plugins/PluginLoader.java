package net.hopskocz.mmc.Plugins;

import net.hopskocz.mmc.Logger.Logger;
import net.hopskocz.mmc.Options.Options;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by hopskocz on 25.04.14.
 */
public class PluginLoader {
    ClassLoader classLoader;

    public PluginLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        Logger.info("[PluginLoader]: Initialization successful");
    }

    public ArrayList<Class> loadPlugins() throws MalformedURLException, ClassNotFoundException {
        ArrayList<Class> objectList = new ArrayList<Class>();
        ArrayList<URL> pluginsURLs = new ArrayList<URL>();
        ArrayList<String> plugins = new ArrayList<String>();

        //list plugins
        File folder = new File(System.getProperty("user.dir") + "/Plugins/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".jar")) {
                System.out.println("Plugin found: " + listOfFiles[i].getName());
                pluginsURLs.add(new URL("file://" + listOfFiles[i].getAbsolutePath()));
                plugins.add(listOfFiles[i].getName().replace(".jar", ""));
            }
        }

        URL[] classUrls = pluginsURLs.toArray(new URL[pluginsURLs.size()]);
        URLClassLoader ucl = new URLClassLoader(classUrls);

        for (Iterator<String> itr = plugins.iterator(); itr.hasNext(); ) {
            objectList.add(ucl.loadClass(itr.next()));
        }

        return objectList;
    }

    public void loadPluginsMoarAwesome(ArrayList<Plugin> pluginList)
            throws MalformedURLException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {

        ArrayList<Class> objectList = new ArrayList<Class>();
        ArrayList<URL> pluginsURLs = new ArrayList<URL>();
        ArrayList<String> plugins = new ArrayList<String>();

        //list plugins
        File folder = new File(Options.getGamePath() + "/Plugins/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".jar")) {
                    Logger.info("[PluginLoader]: Plugin found: "+ listOfFiles[i].getName());
                    pluginsURLs.add(listOfFiles[i].toURI().toURL());
                    plugins.add(listOfFiles[i].getName().replace(".jar", ""));
                }
            }


            URL[] classUrls = pluginsURLs.toArray(new URL[pluginsURLs.size()]);
            URLClassLoader ucl = new URLClassLoader(classUrls);

            Class tempClass;

            for (Iterator<String> itr = plugins.iterator(); itr.hasNext(); ) {
                //objectList.add(ucl.loadClass(itr.next()));
                tempClass = ucl.loadClass(itr.next());
                pluginList.add(new Plugin(tempClass, tempClass.getConstructor().newInstance()));
            }

        } else
            Logger.info("[PluginLoader]: Cannot find any plugin");
    }
}