package com.example.plugin.listeners;

import com.example.plugin.Bedwars;
import com.example.plugin.controllers.BedwarsInGameQueueController;
import com.example.plugin.controllers.BedwarsInGameQueueController;
import com.example.plugin.managers.BedwarsPlayerManager;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: Test this out.
public class PlayerJoinLeaveSystem extends RefSystem<EntityStore> {


    private BedwarsInGameQueueController queueController;
    private Bedwars plugin;

    public PlayerJoinLeaveSystem(Bedwars plugin) {
        this.plugin = plugin;
    }

    /**
     * Register this query as apart of a Player.
     */
    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }

    /**
     *  When a player joins the game, update queue logic and welcome player.
     *
     * @param ref Reference to the player.
     * @param addReason Reason of joining the world (Connecting, Warping, etc.).
     * @param store World's entity store.
     * @param commandBuffer Command buffer.
     */
    @Override
    public void onEntityAdded(@Nonnull Ref<EntityStore> ref, @Nonnull AddReason addReason, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Player player = (Player) store.getComponent(ref, Player.getComponentType());

        if (!plugin.gameCommenced()) {

            if (player != null && player.isFirstSpawn()) {
                player.sendMessage(Message.raw("Welcome to Hytale Bedwars!"));
            } else {
                player.sendMessage(Message.raw("Welcome back!"));
            }

            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            queueController.addPlayer(ref, player);
        } else {
            if (BedwarsPlayerManager.contains(ref)) {
                if (BedwarsPlayerManager.get(ref).canRejoin()) {
                    // Presume the player dead or eliminated.
                }
            } else {
                // player.getWorld().drainPlayersTo();
                // Send back to server lobby.
            }
        }

        // Execute if game is enqueued
        // Update if queue should countdown/game start, notify world of player joins.

        // Execute once game is commenced.
        // Kick players who are not eligible for rejoin.
        // Those eligible to rejoin are those that joined during queue and left during the game.

        // Execute upon eligible rejoin
        // Player will be killed or eliminated depending on the bed's state.

    }

    /**
     *  When a player leaves the game, update queue logic.
     *
     * @param ref Reference to the player.
     * @param removeReason Reason of leaving the world (Kick/Ban, Warping, Disconnect, etc.).
     * @param store World's entity store.
     * @param commandBuffer Command buffer.
     */
    @Override
    public void onEntityRemove(@Nonnull Ref<EntityStore> ref, @Nonnull RemoveReason removeReason, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Player player = (Player) store.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());

        if (!plugin.gameCommenced()) {
            BedwarsPlayerManager.remove(ref);
        }

        //player.sendMessage(Message.raw("" + playerRef));

        // Execute during game queue
        // Player will leave the lobby and won't be able to rejoin the same world once its game has started.

        // Execute when leaving during game
        // Having stayed through the entire queue, the player is eligible to rejoin, and will be deemed dead/eliminated depending on the bed's state.
    }
}
