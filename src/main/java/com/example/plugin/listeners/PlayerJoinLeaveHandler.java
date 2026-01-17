package com.example.plugin.listeners;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: Test this out.
public class PlayerJoinLeaveHandler extends RefSystem {
    @Override
    public void onEntityAdded(@Nonnull Ref ref, @Nonnull AddReason addReason, @Nonnull Store store, @Nonnull CommandBuffer commandBuffer) {
        Player player = (Player) commandBuffer.getComponent(ref, Player.getComponentType());

        if (player.isFirstSpawn()) {
            player.sendMessage(Message.raw("Welcome to Hytale Bedwars!"));
        } else {
            player.sendMessage(Message.raw("Welcome back!"));
        }

        // Execute if game is enqueued
        // Update if queue should countdown/game start, notify world of player joins.

        // Execute once game is commenced.
        // Kick players who are not eligible for rejoin.
        // Those eligible to rejoin are those that joined during queue and left during the game.

        // Execute upon eligible rejoin
        // Player will be killed or eliminated depending on the bed's state.

    }

    @Override
    public void onEntityRemove(@Nonnull Ref ref, @Nonnull RemoveReason removeReason, @Nonnull Store store, @Nonnull CommandBuffer commandBuffer) {
        Player player = (Player) store.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());

        if (playerRef != null) {
            System.out.println(playerRef.getUsername() + " has left the game.");
        }

        // Execute during game queue
        // Player will leave the lobby and won't be able to rejoin the same world once its game has started.

        // Execute when leaving during game
        // Having stayed through the entire queue, the player is eligible to rejoin, and will be deemed dead/eliminated depending on the bed's state.
    }

    @Nullable
    @Override
    public Query getQuery() {
        return Player.getComponentType();
    }
}
