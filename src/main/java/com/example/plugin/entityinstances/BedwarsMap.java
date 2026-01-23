package com.example.plugin.entityinstances;

import com.example.plugin.managers.BedwarsTeamsManager;
import com.example.plugin.utils.GAMEMODE;
import com.example.plugin.utils.TeamColor;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


///  @author ooney
public class BedwarsMap {


    private World world;
    private Vector3d queueSpawn;
    private ArrayList<BedwarsMidResource> midResources; // Resources at mid (Diamond, Emerald)
    private BedwarsTeamsManager teamsManager;
    private GAMEMODE gamemode;

    public static final Map<String, GAMEMODE> possibleGamemodes = Map.of(
            "Solos (1v1s)", GAMEMODE.ONES,
            "Doubles (2v2s)", GAMEMODE.TWOS,
            "Trios (3v3s)", GAMEMODE.THREES,
            "Squads (4v4s)", GAMEMODE.FOURS,
            "Squad v Squad (4v4)", GAMEMODE.FOURAFOUR
    );

    // Get which teams are valid for the selected gamemode
    public static final Map<GAMEMODE, List<TeamColor>> teamsPerGamemode =
            Map.of(
                    GAMEMODE.ONES, List.of(TeamColor.values()),
                    GAMEMODE.TWOS, List.of(TeamColor.values()),
                    GAMEMODE.THREES, List.of(TeamColor.values()),
                    GAMEMODE.FOURS, List.of(TeamColor.values()),
                    GAMEMODE.FOURAFOUR, List.of(TeamColor.RED, TeamColor.BLUE)
            );

    // Set as null or default at first, the manager will run checks and the user will do setters on deployment.
    // This is so that the UI can throw errors upon deployment.
    public BedwarsMap() {
        queueSpawn = new Vector3d();
        midResources = new ArrayList<>();
        gamemode = null;
    }

    public BedwarsMap(World world) {
        this();
        if (world != null) {
            this.world = world;
        } else {
            throw new NullPointerException("World null when attempting to register a BedwarsMap.");
        }
    }

    /** Setter for the queueSpawn Vector3d, reads in doubles directly instead of a Vector3d.
     *
     * @param x SE
     * @param y SE
     * @param z SE
     */
    public void registerMapsQueueSpawn(double x, double y, double z) {
        queueSpawn.setX(x); queueSpawn.setY(y); queueSpawn.setZ(z);
    }


    ///  REGISTER SETTERS

    /** Add a mid resource into the map.
     *
     * @param resource
     */
    public void registerMapsMidResource(BedwarsMidResource resource) {
        midResources.add(resource);
    }

    /** Add a team to the map.
     *
     */
    public void registerTeamToMap(BedwarsTeam team) {
        teamsManager.addToTeam(team);
    }


    ///  REGISTER REMOVERS

    public void removeMapsMidResource(BedwarsMidResource resource) {
        midResources.remove(resource);
    }

    public void removeMapsMidResource(int index) {
        midResources.remove(index);
    }

    public void removeTeamFromMap(BedwarsTeam team) {
        teamsManager.removeTeam(team);
    }



    ///  GETTERS  ///

    public BedwarsTeam getTeam(BedwarsTeam team) {
        return teamsManager.getTeam(team);
    }

    public ArrayList<BedwarsTeam> getTeams() {
        return teamsManager.getTeams();
    }

    public BedwarsMidResource getMidResource(BedwarsMidResource resource) {
        for (BedwarsMidResource r : midResources) {
            if (r.equals(resource)) {
                return r;
            }
        }
        return null;
    }

    ///  GET/SET WORLD

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /// GET/SET GAMEMODE

    public GAMEMODE getGamemode() {
        return gamemode;
    }

    public void setGamemodeFromString(String s) {
        if (!possibleGamemodes.containsKey(s)) {
            throw new NoSuchElementException("There is no mapping that \"" + s + "\" covers.");
        }
        gamemode = possibleGamemodes.get(s);
    }

    public void setGamemode(GAMEMODE gamemode) {
        this.gamemode = gamemode;
    }

    /// GET/SET QUEUE SPAWN

    public Vector3d getQueueSpawn() {
        return queueSpawn;
    }

    public void setQueueSpawn(Vector3d queueSpawn) {
        this.queueSpawn = queueSpawn;
    }

}
