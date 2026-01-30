package com.example.plugin.utils;

import com.example.plugin.managers.BedwarsPlayerManager;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.logging.Level;

/// @author ooney
public class BedwarsLogger {

    public static HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static void logLeftGame(Player player, BedwarsPlayerManager playerManager, boolean terminate) {
        if (terminate) {
            LOGGER.at(Level.INFO).log(player.getDisplayName().toUpperCase() + " LEFT THE GAME BEFORE THE GAME! NULLIFYING AND REMOVING...");
        } else {
            String isUUIDSaved = playerManager.get(player).getUuid() == null ? " THEIR UUID WAS NOT SAVED DURING THEIR SESSION!" : " THEIR UUID IS SAVED TO THEIR BEDWARSPLAYER!";
            LOGGER.at(Level.WARNING).log(player.getDisplayName().toUpperCase() + " LEFT THE GAME DURING THE GAME!" + isUUIDSaved);
        }
    }

    public static void logStartGame(World world) {
        LOGGER.at(Level.FINEST).log("GAME HAS STARTED ON WORLD " + world.getName() + "!");
    }

    public static void logEndGame(World world) {
        LOGGER.at(Level.FINEST).log("GAME HAS ENDED ON WORLD " + world.getName() + "!");
    }

    public static void logStartQueueCountdown(World world) {
        LOGGER.at(Level.FINE).log("QUEUE COUNTDOWN STARTED ON WORLD" + world.getName() + "!");
    }

    public static void logCancelQueueCountdown(World world) {
        LOGGER.at(Level.FINE).log("QUEUE COUNTDOWN CANCELLED ON WORLD" + world.getName() + "!");
    }

    public static void executedDeployCommand(Player player) {
        LOGGER.at(Level.WARNING).log(player.getDisplayName().toUpperCase() + " HAS STARTED DEPLOYING THE WORLD AS A MAP.");
    }

    public static void executedActivateMap(Player player) {
        LOGGER.at(Level.WARNING).log(player.getDisplayName().toUpperCase() + " HAS ACTIVATED THE MAP!");
    }



}
