package com.example.plugin.controllers;

import com.example.plugin.Bedwars;
import com.example.plugin.managers.BedwarsPlayer;
import com.example.plugin.managers.BedwarsPlayerManager;
import com.example.plugin.messenger.BedwarsMessenger;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

///  @author ooney

/**
 * For the queue system in the pre-game.
 */
public class BedwarsInGameQueueController {

    private int startTime = 30;
    private int secondsRemaining = startTime;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Bedwars plugin;

    public BedwarsInGameQueueController(Bedwars plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a player to the list of BedwarsPlayers.
     * If and only if they are eligible or the queue is in session.
     *
     * @param ref
     * @param player
     */
    public void addPlayer(Ref<EntityStore> ref, Player player) {
         // If game has started, render them dead or eliminated.
            BedwarsMessenger.notEnoughPlayersMessage(player);
            BedwarsPlayerManager.add(ref, player);
            updateQueue();
        // player.getWorld().drainPlayersTo(); // Send them back to server lobby.
    }

    /**
     * Remove a player from the list of BedwarsPlayers.
     * Will truly remove the player if they are ineligible to rejoin.
     *
     * @param ref
     */
    public void removePlayer(Ref<EntityStore> ref) {
        BedwarsPlayer player = BedwarsPlayerManager.get(ref);
        BedwarsPlayerManager.remove(ref);
        updateQueue();
    }

    /**
     * Interacts with the game countdown based on how many players are in the lobby.
     */
    public void updateQueue() {
        // Start countdown once 3 or more players join.
        if (BedwarsPlayerManager.getSize() >= 3) { // Start a countdown to the game.
            startOrCompleteCountdown(false);
        } else if (BedwarsPlayerManager.getSize() == 8) { // Force start the game
            startOrCompleteCountdown(true);
        } else { // < 3 players ; stop and reset the countdown
            stopCountdown();
        }
    }

    /** Starts a countdown, if completed, it will send the users to their spawns.
     *
     * @param complete If the timer should automatically complete, due to full players.
     */
    public void startOrCompleteCountdown(boolean complete) {
        if (complete) {
            stopCountdown();
            for (Ref<EntityStore> player : BedwarsPlayerManager.getIndexOfPlayers()) {
                BedwarsMessenger.queueDoneTestMessage(BedwarsPlayerManager.get(player).getPlayer());
            }
            // Call to send players to game, select teams, start ticking resources, make eligible for rejoin, etc.
            // TODO: Register the first map, then develop this.
            // start ticking resources...

        }
        startCountdown();
    }

    /**
     * Starts a countdown to start the game to allow more people to join.
     */
    public void startCountdown() {

        secondsRemaining = startTime; // Reset to start time, assuming secondsRemaining will stay decremented after any potential reset.

        BedwarsMessenger.queueTimeRemaining(secondsRemaining);
        scheduler.scheduleAtFixedRate(() -> {
            if (secondsRemaining > 0) {
                secondsRemaining--;
                BedwarsMessenger.queueTimeRemaining(secondsRemaining);
            } else {
                // Entering game...
                startOrCompleteCountdown(true);
            }
        },1,1, TimeUnit.SECONDS);
    }

    /**
     * Completely stops the game countdown, notifies players of it.
     */
    public void stopCountdown() {
        scheduler.shutdownNow();
        // Send message to all players
    }



}
