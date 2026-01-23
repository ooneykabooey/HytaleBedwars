package com.example.plugin.managers;

import com.example.plugin.entityinstances.BedwarsTeam;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;

/// @author ooney

public class BedwarsTeamsManager {

    public static final String[] DEFAULT_TEAM_COLORS = {"red", "orange", "yellow", "green", "aqua", "pink", "white", "blue"};
    public ArrayList<BedwarsTeam> teams = null; //TODO: admin needs to determine team name, color, and spawn/resource coords.

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

}
