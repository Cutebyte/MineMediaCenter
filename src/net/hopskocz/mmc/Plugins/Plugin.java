package net.hopskocz.mmc.Plugins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class should be used as extender to plugins
 * Every plugins that is child of this have functions
 * that allow them to be loaded, and used in MMC
 *
 * Sry. English isn't my first language. At least I'm still learning.
 */
public class Plugin {
    protected String name;
    protected String description;
    protected String type;
    protected boolean haveInterface;
    protected Class pluginClass;
    protected Object pluginObject;


    public Plugin(Class pluginClass, Object pluginObject) {
        this.pluginClass = pluginClass;
        this.pluginObject = pluginObject;

        //loadSmthng
        try {
            name = (String)getMethod("getName").invoke(pluginObject);
            description = (String)getMethod("getDescription").invoke(pluginObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Method[] getMethods() {
        return this.getClass().getMethods(); //awesome isn't? @,@
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public boolean haveInterface() {
        try {
            pluginClass.getMethod("createInterface");
        } catch (NoSuchMethodException e) {
            System.out.println("This plugin does not contain interface");
            return false;
        }
        return true;
    }

    public Method getMethod(String methodName) {
        try {
            return pluginClass.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getObject() {
        return pluginObject;
    }
}
