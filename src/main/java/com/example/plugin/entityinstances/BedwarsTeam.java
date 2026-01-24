package com.example.plugin.entityinstances;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

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

    // ECS-entity references instead of UUIDs
    private final Set<Ref<EntityStore>> players = new HashSet<>();

    public BedwarsTeam(String id, Vector3d spawnLocation, Vector3d forgeLocation, Vector3d bedLocation) {
        this.id = id;
        this.spawnLocation = spawnLocation;
        this.forgeLocation = forgeLocation;
        this.bedLocation = bedLocation;
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

    public void destroyBed() {
        bedAlive = false;
    }

    /**
     * Adds a player to this team.
     * @param ref the ECS entity reference of the player
     */
    public void addPlayer(Ref<EntityStore> ref) {
        players.add(ref);
    }

    /**
     * Removes a player from this team.
     * @param ref the ECS entity reference of the player
     */
    public void removePlayer(Ref<EntityStore> ref) {
        players.remove(ref);
    }

    /**
     * Returns all players currently on this team.
     */
    public Set<Ref<EntityStore>> getPlayers() {
        return players;
    }

    /**
     * Optional helper: check if a player is on this team
     */
    public boolean containsPlayer(Ref<EntityStore> ref) {
        return players.contains(ref);
    }


    /// TEAM SETTERS

    public void setID(String id) {
        this.id = id;
    }

    public void setSpawnLocation(Vector3d vector) {
        this.spawnLocation = vector;
    }

    public void setForgeLocation(Vector3d vector) {
        this.forgeLocation = vector;
    }

    // BED SETTER/GETTER

    public Vector3d getBedLocation() {
        return bedLocation;
    }

    public void setBedLocation(Vector3d bedLocation) {
        this.bedLocation = bedLocation;
    }


    /// UPDATE TEAM

    public void updateTeam(Vector3i anyBlockBroken) {

        ///  If the anyBlockBroken value from the parameter matches the bedLocation, turn off respawning.
        if (anyBlockBroken != null && anyBlockBroken.toVector3d().equals(this.bedLocation)) {
            this.bedAlive = false;
            // TODO: Notify Team Members.
        }

    }



}
