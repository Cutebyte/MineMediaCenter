package net.hopskocz.mmc.Plugins;

import java.util.ArrayList;

/**
 * Created by hopskocz on 25.04.14.
 */
public class PluginList {

    private ArrayList<Plugin> pluginList;

    public PluginList() {
        pluginList = new ArrayList<Plugin>();
    }

    public void add(Plugin plugin) {
        pluginList.add(plugin);
    }
}
