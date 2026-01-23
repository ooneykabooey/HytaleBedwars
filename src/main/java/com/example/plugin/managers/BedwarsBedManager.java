package com.example.plugin.managers;

import com.example.plugin.entityinstances.BedwarsTeam;
import com.hypixel.hytale.math.vector.Vector3d;
import java.util.HashMap;
import java.util.Map;

/// @author yasha

/**
 * Central authority for tracking all beds in the BedWars game.
 */
public class BedwarsBedManager {

    // Map bed block positions to the team that owns the bed
    private final Map<Vector3d, BedwarsTeam> bedLocations = new HashMap<>();

    /**
     * Register a bed for a team.
     *
     * @param pos  the block position of the bed
     * @param team the team that owns this bed
     */
    public void registerBed(Vector3d pos, BedwarsTeam team) {
        bedLocations.put(pos, team);
    }

    /**
     * Called when a bed block is broken.
     * Marks the bed as destroyed for the owning team.
     *
     * @param pos the position of the broken bed
     */
    public void handleBedBreak(Vector3d pos) {
        BedwarsTeam team = bedLocations.get(pos);
        if (team == null) return; // Not a tracked bed

        // Destroy the bed
        team.destroyBed();

        // Remove from tracking
        bedLocations.remove(pos);

        // TODO: Optional - broadcast message to players
        // Example: sendMessage(team.getId() + "'s bed was destroyed!");
    }

    /**
     * Check if a given block position is a bed.
     */
    public boolean isBed(Vector3d pos) {
        return bedLocations.containsKey(pos);
    }

    /**
     * Get the team that owns the bed at the given position.
     */
    public BedwarsTeam getTeam(Vector3d pos) {
        return bedLocations.get(pos);
    }

    /**
     * Optional: Clear all beds (e.g., when restarting the game)
     */
    public void reset() {
        bedLocations.clear();
    }
}
