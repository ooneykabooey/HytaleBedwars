package com.example.plugin.managers;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.file.BedwarsMapIO;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

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

    public void tryBindMapsToWorlds() {
        for (BedwarsMap map : maps.values()) {
            if (map.getWorld() != null) continue;

            World world = Universe.get().getWorld(map.getWorldName());
            if (world != null) {
                map.setWorld(world);
                Bedwars.registerMap(map);

                LOGGER.atInfo().log("Bound bedwars map " + map.getWorldName() + " to world " + world.getName());

                LOGGER.atInfo().log("Map: " + map.getWorldName() + " world: " + map.getWorld());
            }
        }
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
                        }
                    });
        } catch (Exception e) {
            LOGGER.atInfo().log("Failed to load maps {}", e);
        }

        tryBindMapsToWorlds();

    }
    
    public BedwarsMap getMap(String name) {
        return maps.get(name);
    }
}