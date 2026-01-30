package com.example.plugin.entityinstances;

import com.example.plugin.Bedwars;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.entityinstances.BedwarsItemTimer;
import com.example.plugin.utils.TeamColor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/// @author yasha, ooney

/**
 * Represents a BedWars team and tracks its state.
 */
public class BedwarsTeam {

    private String id; // "red", "blue", etc
    private Vector3d spawnLocation;
    private boolean bedAlive = true;
    private Vector3d forgeLocation;
    private Vector3d bedLocation;
    private ArrayList<BedwarsItemTimer> forges = new ArrayList<>();
    private Color color;
    private BedwarsMap thisMap;

    // TODO: Probably make this an ArrayList or ArraySet instead.
    private final ArrayList<BedwarsPlayer> players = new ArrayList<>();

    public BedwarsTeam(String id, Vector3d spawnLocation, Vector3d forgeLocation, Vector3d bedLocation, BedwarsMap map, Color color) {
        this.id = id;
        this.spawnLocation = spawnLocation;
        this.forgeLocation = forgeLocation;
        this.bedLocation = bedLocation;
        this.thisMap = map;
        this.color = color;

        // Initialize forges immediately so they work when loaded from config
        initializeTeamForges(forgeLocation, null, map);
    }

    public String getId() {
        return id;
    }

    public Vector3d getSpawnLocation() {
        return spawnLocation;
    }

    public boolean hasBed() {
        return bedAlive;
    }


    /**
     * Adds a player to this team.
     */
    public void addPlayer(BedwarsPlayer player) {
        players.add(player);
    }


    /**
     * Removes a player from this team.
     * @param ref the ECS entity reference of the player
     */
    public void removePlayer(Ref<EntityStore> ref) {
        players.removeIf(p -> p.getRef().equals(ref));
    }

    /**
     * Returns all players currently on this team.
     */
    public ArrayList<BedwarsPlayer> getPlayers() {
        return players;
    }

    /**
     * Optional helper: check if a player is on this team
     */
    public boolean containsPlayer(Ref<EntityStore> ref) {
        return players.stream().anyMatch(p -> p.getRef().equals(ref));
    }

    /**
     * Clear all players from the players list.
     */
    public void clearPlayerList() {
        players.clear();
    }

    public Vector3d getForgeLocation() {
        return forgeLocation;
    }


    /// TEAM SETTERS

    public void setID(String id) {
        this.id = id;
    }

    public void setSpawnLocation(Vector3d vector) {
        this.spawnLocation = vector;
    }

    public void setForgeLocation(Vector3d vector, Player player) {
        this.forgeLocation = vector;
        if (forges.isEmpty()) {
            initializeTeamForges(forgeLocation, player, thisMap);
        }
    }

    // BED SETTER/GETTER

    public Vector3d getBedLocation() {
        return bedLocation;
    }

    public void setBedLocation(Vector3d bedLocation) {
        this.bedLocation = bedLocation;
    }


    /// UPDATE TEAM

    public void updateTeam(Vector3i anyBlockBroken, BlockType blockType, BedwarsPlayer perpetrator) {

        ///  If the anyBlockBroken value from the parameter matches the bedLocation, turn off respawning.
        if (anyBlockBroken != null && anyBlockBroken.toVector3d().equals(this.bedLocation) && blockType.getId().equals("Furniture_Crude_Bed")) {
            this.bedAlive = false;
            BedwarsMessenger.bedDestroyed(this, thisMap, perpetrator);
        }

    }

    ///  INITIALIZE FORGES

    public void initializeTeamForges(Vector3d vector, Player player, BedwarsMap map) {
        forges.add(new BedwarsItemTimer("GOLD", 8, new BedwarsItemTimer.DropEntry("Ingredient_Bar_Gold", 1), vector, thisMap, this, false));
        forges.add(new BedwarsItemTimer("IRON", 1, new BedwarsItemTimer.DropEntry("Ingredient_Bar_Iron", 1), vector, thisMap, this, false));

        assert map != null;
        assert map.getResourceTimer() != null;

        map.addAllToResourceTimer(forges);
    }


    ///  GET COLOR

    public Color getColor(){
        return color;
    }

}
