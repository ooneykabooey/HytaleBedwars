package com.example.plugin.managers;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;

import java.util.ArrayList;

///  @author ooney
public class BedwarsMapManager {

    private Bedwars plugin;
    public static ArrayList<BedwarsMap> mapEntries = new ArrayList<>();


    public BedwarsMapManager(Bedwars plugin) {
        this.plugin = plugin;
    }

    public Bedwars getPlugin() {
        return plugin;
    }

    public void addToMapEntries(BedwarsMap map) {
        map.setPlugin(plugin);
        mapEntries.add(map);
    }

}
