package com.example.plugin.managers;

import com.example.plugin.Bedwars;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/// @author ooney

public class BedwarsTeamsManager {

    public static final String[] TEAM_COLORS = {"red", "orange", "yellow", "green", "aqua", "pink", "white", "blue"};
    public List<BedwarsTeam> teams = List.of( // TODO: Have the Vector3ds be determined by admin.
            new BedwarsTeam("red", new Vector3d(0,0,0), new Vector3d(0,0,0)),
            new BedwarsTeam("orange", new Vector3d(0,0,0), new Vector3d(0,0,0)),
            new BedwarsTeam("yellow", new Vector3d(0,0,0), new Vector3d(0,0,0)),
            new BedwarsTeam("green", new Vector3d(0,0,0), new Vector3d(0,0,0)),
            new BedwarsTeam("aqua", new Vector3d(0,0,0), new Vector3d(0,0,0)),
            new BedwarsTeam("pink", new Vector3d(0,0,0), new Vector3d(0,0,0)),
            new BedwarsTeam("white", new Vector3d(0,0,0), new Vector3d(0,0,0)),
            new BedwarsTeam("blue", new Vector3d(0,0,0), new Vector3d(0,0,0))
    );

    // TODO: If Threes or Fours, only have red, blue, green, and yellow in the list to pick from.
    // TODO: If players are in a party together, make sure they are always on the same team.
    // TODO: From server lobby, prevent players from joining modes with too high of a party, UNLESS its ones.
    // TODO: Add a check for full occupation within a team, depends on the mode as well.

    // Give the players their team assignments.
    public void initializeTeams() {
        for (Ref<EntityStore> playerKey : BedwarsPlayerManager.getIndexOfPlayers()) {

            // Random number 0,7, to pick the index of team.
            BedwarsPlayerManager.get(playerKey).setTeam(teams.get((int) Math.floor(Math.random() * 8)));

        }
    }

}
