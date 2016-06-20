package net.hopskocz.mmc.Plugins;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * Created by Tomek on 2014-09-05.
 */
public abstract class PluginBase {
    protected String name;
    protected String description;
    protected int type;
    protected JPanel mainPanel;

    public PluginBase() {

    }

    public JPanel createInterface() {
        return mainPanel;
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

    public int getType() {
        return type;
    }
}
