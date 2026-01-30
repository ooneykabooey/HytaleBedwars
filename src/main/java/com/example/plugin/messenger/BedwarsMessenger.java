package com.example.plugin.messenger;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsPlayer;
import com.example.plugin.entityinstances.BedwarsTeam;
import com.example.plugin.managers.BedwarsPlayerManager;
import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import java.awt.*;


///  @author ooney

public class BedwarsMessenger {

    private Bedwars plugin;
    private static boolean debug;

    public void setDebug(boolean value) {
        debug = value;
    }

    public boolean getDebug() {
        return debug;
    }

    public BedwarsMessenger(Bedwars plugin) {
        this.plugin = plugin;
    }

    /**
     * Prompts the user that they do not have permission to use the command.
     * @return
     */
    public static void noPermission(Player player) {
        player.sendMessage(Message.raw("You do not have permission to execute this command!"));
    }

    public static void noPermission(CommandContext context) {
        context.sendMessage(Message.raw("You do not have permission to execute this command!"));
    }

    public static void queueTimeRemaining(int secondsRemaining, World world) {
        EventTitleUtil.showEventTitleToWorld(
                Message.raw(secondsRemaining + " seconds.."),
                Message.raw( "TIME UNTIL GAME STARTS"),
                true,
                null,
                1,
                0,
                0,
                world.getEntityStore().getStore());

    }


    ///  --------------------- ///
    ///  ---- SERVER PROMPTS --- ///
    ///  --------------------- ///


    /** Send a message to the player that they do not have permission to run a command.
     *
     * @param player
     */
    public static void notEnoughPlayersMessage(Player player) {
        player.sendMessage(Message.raw("Three or more people required to play."));
    }

    public static void queueCancelled(BedwarsMap map) {
        for (BedwarsPlayer player : map.getPlayerManager().getAll()) {
            player.getPlayer().sendMessage(Message.raw("Not enough players to continue! Countdown cancelled!"));
        }
    }

    public static void queueStarted(BedwarsMap map) {
        for (BedwarsPlayer player : map.getPlayerManager().getAll()) {
            player.getPlayer().sendMessage(Message.raw("We have enough players! Countdown started!"));
        }
    }

    public static void promptUserToDeploy(Player player) {
        player.sendMessage(Message.raw("Welcome, this world has not been registered as a bedwars map, do /deploy to do so!"));
    }

    public static void bedDestroyed(BedwarsTeam team, BedwarsMap map, BedwarsPlayer perpetrator) {
        for (BedwarsPlayer player : team.getPlayers()) {
            EventTitleUtil.showEventTitleToPlayer(player.getPlayerRef(), Message.raw("BED DESTROYED!"), Message.raw("You can no longer respawn!"), true);
        }
        for (BedwarsPlayer player : map.getPlayerManager().getAll()) {
            player.getPlayer().sendMessage(Message.raw(team.getId() + "'s bed has been destroyed!").color(Color.RED));
            player.getPlayer().sendMessage(Message.raw("Destroyed by: " + perpetrator.getPlayer().getDisplayName() + "!").color(perpetrator.getTeam().getColor()));
        }
    }

    public static void cannotDestroyOwnBed(Player player) {
        player.sendMessage(Message.raw("You cannot destroy your own bed!").color(Color.RED));
    }

    ///  --------------------- ///
    ///  ---- DEBUG PROMPTS ---- ///
    ///  --------------------- ///
    ///  @see com.example.plugin.Bedwars <-- if debug == true, enable these.

    /** Switch debug mode, notify player who executed command.
     *
     * @param context <-- Whoever sent command (Player, server, etc.)
     */
    public static void toggleDebug(CommandContext context) {
        if (context.sender().hasPermission("bedwars.op")) {
            debug = !debug;
            context.sendMessage(Message.raw(debug == true ? "Activated " : "Deactivated " + " debug mode!"));
        } else {
            noPermission(context);
        }
    }


    ///  ----- COMMAND -------- ///

    public static void activateMapExecutionFail(CommandContext context) {
        context.sendMessage(Message.raw("You must register this map as a bedwars map before activating!"));
    }


    /**
     * Tells the user what gamemodes there are.
     * DEPRECATED --- Use the UI.
     */
    public static void deployHelp(CommandContext context) {

        if (debug) {
            // TODO: String-builder with a for-loop to prevent long strings, also make multiple info pages when there are enough gamemodes.
            context.sendMessage(Message.raw(            "/deploy \n\t"+
                    " 1vx8 <- eight player FFA\n\t"+
                    "2vx8 <- teams of two\n\t"+
                    "3vx4 <- teams of three\n\t"+
                    "4vx4 <- teams of four\n\t"+
                    "4v4 <- four against four\n\t").color(Color.GREEN));

        }
        context.sendMessage(Message.raw("Activate \"debug mode\" to see /deploy's info. (Deprecated command)").color(Color.GREEN));
    }

    /**
     * Tells the user they have deployed x gamemode.
     * DEPRECATED --- Use the UI.
     */
    public static void deployedMessage(CommandContext context, GAMEMODE mode) {
        if (debug) {
            String output = "";
            switch (mode) {
                case ONES: output = "1v1 x8" ; break;
                case TWOS: output = "2v2 x8"; break;
                case THREES: output = "3v3 x4"; break;
                case FOURS: output = "4v4 x4"; break;
                case FOURAFOUR: output = "4v4"; break;
            }
            context.sendMessage(Message.raw("You have deployed Bedwars into the world with mode " + output + "!").color(Color.GREEN));

        } else {
        }

    }


    ///  ----- SERVER --------- ///

    // ----- PLAYER MESSAGES ----- //


    /** Send a message to the player when they place a block.
     *
     */
    public static void playerPlacedBlockMessage(Player player) {
        if (debug) {
            player.sendMessage(Message.raw("Placed block!"));
        }
    }

    public static void playerDamagedBlockMessage(Player player) {
        if (debug) {
            player.sendMessage(Message.raw("Damaged block!"));
        }
    }

    public static void notAllowedToBreakMapMessage(Player player) {
        if (debug) {
            player.sendMessage(Message.raw("You cannot break blocks that are apart of the map!"));
        }
    }

    public static void respawnMessage(Player player) {
        if (debug) {
            player.sendMessage(Message.raw("You have respawned!"));
        }
    }

    // ----- MESSAGES FROM GAME ----- //
    /** Sends a message to the player that the queue finished counting and executed what it needed to.
     *
     * DEPRECATED -- Only a test.
     * @param player
     */
    public static void queueDoneTestMessage(Player player) {
        if (debug) {
            player.sendMessage(Message.raw("Celebrate!!! Queue Finished! :D"));
        }
    }

    public static void forgeSpawnMessage(String ID, Player player) {
        if (debug) {
            player.sendMessage(Message.raw(ID + " has dropped at the forge!"));
        }
    }

    /** Notify the user that the forges are ticking.
     *
     */
    public static void notifyForgeTicking(Player player, Store<EntityStore> store) {
        EventTitleUtil.showEventTitleToWorld(
                Message.raw("Ticking :D"),
                Message.raw("Timer commenced."),
                true,
                null,
                5,
                1,
                1,
                store
        );
    }

    ///  UI

    public static void selectedGamemode(Player player, GAMEMODE gamemode) {
        player.sendMessage(Message.raw("Selected gamemode: " + gamemode.name()));
    }

    public static void gamemodeNotSelected(Player player) {
        player.sendMessage(Message.raw("You must select a gamemode before continuing!!!"));
    }

    public static void invalidDoubleEntry(Player player) {
        player.sendMessage(Message.raw("Please only enter valid numbers! Examples: 1, 23, 29.3"));
    }

    public static void coordinateEntry(Player player, double x, double y, double z, String whatFor) {
        player.sendMessage(Message.raw(
                "You entered the coordinates: " + x + ", " + y + " and " + z + " for the " + whatFor + "!"
        ));
    }

    public static void alreadyInitializedTeam(Player player, String ID) {
        player.sendMessage(Message.raw("You have already initialized the " + ID + " team!"));
    }

    public static void maxTeamsInitialized(Player player) {
        player.sendMessage(Message.raw("The limit of teams registered for this gamemode has been reached!"));
    }

    public static void midForgeEntry(Player player, int x, int y) {
        player.sendMessage(Message.raw("You have registered " + x + " diamond forges and " + y + " emerald forges!"));
    }

}
