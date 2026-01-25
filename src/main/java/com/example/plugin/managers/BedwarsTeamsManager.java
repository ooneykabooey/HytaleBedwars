package com.example.plugin.managers;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsPlayer;
import com.example.plugin.entityinstances.BedwarsTeam;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;

/// @author ooney

public class BedwarsTeamsManager {

    private BedwarsMap thisMap;
    public ArrayList<BedwarsTeam> teams = new ArrayList<>();
    private BedwarsPlayerManager playerManager;

    public BedwarsTeamsManager(BedwarsPlayerManager playerManager, BedwarsMap thisMap) {
        this.playerManager = playerManager;
    }


    // Give the players their team assignments.
    public void initializeTeams(BedwarsMap map) {
        for (Ref<EntityStore> playerKey : playerManager.getIndexOfPlayers()) {
            playerManager.get(playerKey).setTeam(teams.get((int) Math.floor(Math.random() * teams.size())));
        }
        teleportPlayersToTeamSpawnLocations(map);
    }

    public void addToTeam(BedwarsTeam team) {
        teams.add(team);
    }

    public void removeTeam(BedwarsTeam team) {
        teams.remove(team);
    }

    public BedwarsTeam getTeam(BedwarsTeam team) {
        for (BedwarsTeam t : teams) {
            if (t.equals(team)) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<BedwarsTeam> getTeams() {
        return teams;
    }

    public void updateTeams(Vector3i anyBlockBroken) {
        for (BedwarsTeam team : teams) {
            team.updateTeam(anyBlockBroken);
        }
    }

    public void teleportPlayersToTeamSpawnLocations(BedwarsMap map) {
        World thisWorld = map.getWorld();
        if (thisWorld == null) return;

        for (BedwarsTeam team : teams) {
            for (Ref<EntityStore> playerRef : team.getPlayers()) {
                thisWorld.execute(() -> {
                    if (playerRef == null) return;
                    Store<EntityStore> store = playerRef.getStore();
                    Teleport teleport = Teleport.createForPlayer(thisWorld,
                            team.getSpawnLocation(),
                            new Vector3f(0,0,0));
                    store.addComponent(playerRef, Teleport.getComponentType(), teleport);
                });
            }
        }
    }

    public void setBedwarsMap(BedwarsMap map) {
        thisMap = map;
    }

    public BedwarsMap getThisMap() {
        return thisMap;
    }

}
