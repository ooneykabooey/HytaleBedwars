package com.example.plugin.entityinstances;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

/// @author yasha, ooney

public class BedwarsPlayer {

    private final Ref<EntityStore> ref;
    private final PlayerRef playerRef;
    private final Player player;
    private final UUID uuid;

    private BedwarsTeam team;
    private boolean eliminated = false;
    private boolean rejoinEligible = false;

    public BedwarsPlayer(Player player, UUID uuid) {
        this.player = player;
        this.ref = player.getReference();
        this.playerRef = (PlayerRef) ref.getStore().getComponent(ref, PlayerRef.getComponentType());
        this.uuid = uuid;
    }

    public Ref<EntityStore> getRef() {
        return ref;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getPlayerFromRef(Ref<EntityStore> ref) {
        assert ref != null : "ref null when trying to run getPlayerFromRef";
        return ref.getStore().getComponent(ref, Player.getComponentType());
    }

    public UUID getUuid() {
        return uuid;
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
            newTeam.addPlayer(this);
        }
    }

    public BedwarsTeam getTeam() {
        return team;
    }

    ///  GET/SET PLAYERREF

    public PlayerRef getPlayerRef() {
        return playerRef;
    }


    /**
     * BedWars respawn rule:
     * Player can respawn only if their team still has a bed.
     */
    public boolean canRespawn() {
        return team != null && team.hasBed();
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }


    // Accessor/Mutator Methods for player rejoin eligibility
    public boolean canRejoin() {
        return rejoinEligible;
    }

    public void setRejoinEligibility(boolean value) {
        rejoinEligible = value;
    }

    /**
     * Kicks the player from the server with a custom message.
     * @param reason the kick message displayed to the client
     */
    public void kick(String reason) {
        if (playerRef != null && playerRef.getPacketHandler().getChannel().isActive()) {
            playerRef.getPacketHandler().disconnect(reason);
        }
    }

}
