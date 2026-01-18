package com.example.plugin.managers;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class BedwarsPlayer {

    private final Ref<EntityStore> ref;
    private final Player player;

    private BedwarsTeam team;
    private boolean spectator = false;
    private boolean rejoinEligible = false;

    public BedwarsPlayer(Ref<EntityStore> ref, Player player) {
        this.ref = ref;
        this.player = player;
    }

    public Ref<EntityStore> getRef() {
        return ref;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Assign the player to a team.
     * Handles removing from the previous team automatically.
     */
    public void setTeam(BedwarsTeam newTeam) {
        // Remove from old team
        if (this.team != null) {
            this.team.removePlayer(ref);
        }

        this.team = newTeam;

        // Add to new team
        if (newTeam != null) {
            newTeam.addPlayer(ref);
        }
    }

    public BedwarsTeam getTeam() {
        return team;
    }

    /**
     * BedWars respawn rule:
     * Player can respawn only if their team still has a bed.
     */
    public boolean canRespawn() {
        return team != null && team.hasBed();
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }


    // Accessor/Mutator Methods for player rejoin eligibility
    public boolean canRejoin() {
        return rejoinEligible;
    }

    public void setRejoinEligibility(boolean value) {
        rejoinEligible = value;
    }

}

