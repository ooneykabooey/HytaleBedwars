package com.example.plugin.managers;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsPlayer;
import com.example.plugin.entityinstances.BedwarsTeam;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;

/// @author ooney

public class BedwarsTeamsManager {

    public ArrayList<BedwarsTeam> teams = new ArrayList<>();
    private BedwarsPlayerManager playerManager;

    public BedwarsTeamsManager(BedwarsPlayerManager playerManager, BedwarsMap thisMap) {
        this.playerManager = playerManager;
    }


    // Give the players their team assignments.
    public void initializeTeams(BedwarsMap map) {
        World world = map.getWorld();
        for (BedwarsPlayer player : playerManager.getAll()) {
            playerManager.get(player).setTeam(teams.get((int) Math.floor(Math.random() * teams.size())));
        }
        teleportPlayersToTeamSpawnLocations(world);
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

    public void updateTeams(Vector3i anyBlockBroken, BlockType blockType, BedwarsPlayer perpetrator) {
        for (BedwarsTeam team : teams) {
            team.updateTeam(anyBlockBroken, blockType, perpetrator);
        }
    }

    public void teleportPlayersToTeamSpawnLocations(World world) {

        Store<EntityStore> store = world.getEntityStore().getStore();


        for (BedwarsTeam team : teams) {
            for (BedwarsPlayer player : team.getPlayers()) {
                Bedwars.teleportTo(player, world, player.getRef(), team.getSpawnLocation(), new Vector3f(), world.getName());
                // TODO: This throws "Incorrect teleportId" when teleporting from the queue spawn.
            }
        }

    }

}
