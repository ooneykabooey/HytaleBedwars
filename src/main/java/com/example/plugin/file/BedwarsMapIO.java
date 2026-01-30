package com.example.plugin.file;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsMidResource;
import com.example.plugin.entityinstances.BedwarsTeam;
import com.example.plugin.utils.GAMEMODE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/// author yasha

public class BedwarsMapIO {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    // Making the Gson, also setting to pretty printing :))
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(BedwarsMap map, Path path) {
        MapData data = new MapData();

        if (map.getWorld() != null) {
            data.worldName = map.getWorld().getName();
        }

        if (map.getGamemode() != null) {
            data.gamemode = map.getGamemode().name();
        }

        if (map.getQueueSpawn() != null) {
            data.queueSpawn = new JsonVector(map.getQueueSpawn());
        }

        for (BedwarsMidResource resource : map.getMidResources()) {
            data.resources.add(new JsonResource(resource));
        }

        for (BedwarsTeam team : map.getTeams()) {
            data.teams.add(new JsonTeam(team));
        }

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            LOGGER.atWarning().log("Failed to save map to {}", path, e);
        }
    }

    public static BedwarsMap load(Path path) {
        if (!Files.exists(path)) {
            LOGGER.atWarning().log("Map file not found: {}", path);
            return null;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            MapData data = GSON.fromJson(reader, MapData.class);
            if (data == null) {
                LOGGER.atWarning().log("Failed to parse map data from: {}", path);
                return null;
            }

            // --- DEBUG LOGGING ---
            LOGGER.atInfo().log(">>> DEBUG: Loading JSON from: " + path.getFileName());
            LOGGER.atInfo().log(">>> JSON worldName: " + data.worldName);
            LOGGER.atInfo().log(">>> JSON gamemode: " + data.gamemode);
            LOGGER.atInfo().log(">>> JSON teams count: " + String.valueOf(data.teams != null ? data.teams.size() : 0));

            if (data.worldName == null) {
                LOGGER.atWarning().log("Map config {} is missing a worldName.", path);
                return null;
            }

            BedwarsMap map = new BedwarsMap();
            map.setWorldName(data.worldName);

            World world = Universe.get().getWorld(data.worldName);
            if (world != null) map.setWorld(world);

            if (data.gamemode != null) {
                try {
                    map.setGamemode(GAMEMODE.valueOf(data.gamemode));
                } catch (IllegalArgumentException ignored) {}
            }

            if (data.queueSpawn != null) {
                map.setQueueSpawn(data.queueSpawn.toVector3d());
            }

            if(data.resources != null) {
                for (JsonResource resourceData : data.resources) {
                    map.registerMapsMidResource(new BedwarsMidResource(
                            resourceData.location.toVector3d(),
                            resourceData.id,
                            resourceData.duration
                    ));
                }
            }

            if (data.teams != null) {
                for (JsonTeam teamData : data.teams) {
                    map.registerTeamToMap(new BedwarsTeam(
                            teamData.id,
                            teamData.spawn.toVector3d(),
                            teamData.forge.toVector3d(),
                            teamData.bed.toVector3d(),
                            map,
                            new Color(teamData.color)
                    ));
                }
            }

            // Force registration here to ensure it reaches the main plugin
            Bedwars.registerMap(map);
            LOGGER.atInfo().log("Successfully loaded map for world: {}", map.getWorldName());
            return map;

        } catch (Exception e) {
            LOGGER.atWarning().log("Exception while loading map: " + path + e);
            return null;
        }
    }

    private static class MapData {
        String worldName;
        String gamemode;
        JsonVector queueSpawn;
        List<JsonResource> resources = new ArrayList<>();
        List<JsonTeam> teams = new ArrayList<>();
    }

    private static class JsonVector {
        double x, y, z;

        JsonVector() {} // Gson?
        JsonVector(Vector3d vec) {
            this.x = vec.x; this.y = vec.y; this.z = vec.z;
        }
        Vector3d toVector3d() { return new Vector3d(x, y, z); }
    }

    private static class JsonResource {
        JsonVector location;
        String id;
        int duration;

        JsonResource() {} // Gson again
        JsonResource(BedwarsMidResource resource) {
            this.id = resource.getID();
            this.location = new JsonVector(resource.getResourceLocation());
            this.duration = resource.getDuration();
        }

        public String getID() {
            return id;
        }

        public JsonVector getLocation() {
            return location;
        }

        public int getDuration() {
            return duration;
        }
    }

    private static class JsonTeam {
        String id;
        JsonVector spawn, forge, bed;
        int color;

        JsonTeam() {} // TS Frying me bro what
        JsonTeam(BedwarsTeam team) {
            this.id = team.getId();
            this.spawn = new JsonVector(team.getSpawnLocation());
            this.forge = new JsonVector(team.getForgeLocation());
            this.bed = new JsonVector(team.getBedLocation());
            this.color = team.getColor().getRGB();
        }
    }
}