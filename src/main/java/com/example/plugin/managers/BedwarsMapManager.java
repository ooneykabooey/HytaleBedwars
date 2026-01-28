package com.example.plugin.managers;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.file.BedwarsMapIO;
import com.hypixel.hytale.logger.HytaleLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BedwarsMapManager {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final Bedwars plugin;
    private final Map<String, BedwarsMap> maps = new ConcurrentHashMap<>();
    private final Path mapsDir;

    public BedwarsMapManager(Bedwars plugin) {
        this.plugin = plugin;
        // Saves to: server_root/bedwars_maps/
        this.mapsDir = Path.of("bedwars_maps");
    }

    public void saveMap(String name, BedwarsMap map) {
        Path path = mapsDir.resolve(name + ".json");
        BedwarsMapIO.save(map, path);
        LOGGER.atInfo().log("Saved Bedwars map '{}' to {}", name, path);
    }

    public void loadAllMaps() {
        if (!Files.exists(mapsDir)) return;

        try (var stream = Files.list(mapsDir)) {
            stream.filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        BedwarsMap map = BedwarsMapIO.load(path);
                        if (map != null) {
                            map.setPlugin(plugin);
                            String name = path.getFileName().toString().replace(".json", "");
                            maps.put(name, map);
                            LOGGER.atInfo().log("Loaded Bedwars map '{}' in world '{}'", name, map.getWorld().getName());
                        }
                    });
        } catch (Exception e) {
            LOGGER.atInfo().log("Failed to load maps", e);
        }
    }
    
    public BedwarsMap getMap(String name) {
        return maps.get(name);
    }
}