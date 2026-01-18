package com.example.plugin.managers;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a BedWars team and tracks its state.
 */
public class BedwarsTeam {

    private final String id; // "red", "blue", etc
    private final Vector3d spawnLocation;
    private boolean bedAlive = true;
    private final Vector3d forgeLocation;

    // ECS-entity references instead of UUIDs
    private final Set<Ref<EntityStore>> players = new HashSet<>();

    public BedwarsTeam(String id, Vector3d spawnLocation, Vector3d forgeLocation) {
        this.id = id;
        this.spawnLocation = spawnLocation;
        this.forgeLocation = forgeLocation;
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


}
