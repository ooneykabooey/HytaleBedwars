package com.example.plugin.listeners;

import com.example.plugin.Bedwars;
import com.example.plugin.controllers.BedwarsInGameQueueController;
import com.example.plugin.controllers.BedwarsInGameQueueController;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.managers.BedwarsPlayerManager;
import com.example.plugin.messenger.BedwarsMessenger;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;


/// @author ooney

// TODO: Test this out.
public class PlayerJoinLeaveSystem extends RefSystem<EntityStore> {


    private BedwarsInGameQueueController queueController;
    private Bedwars plugin;
    private BedwarsMap thisMap;

    public PlayerJoinLeaveSystem(Bedwars plugin) {
        this.plugin = plugin;
    }

    /**
     * Register this query as part of a Player.
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
        assert player.getInventory() != null;

        player.getInventory().clear(); // Clear the player's inventory when they join.

        assert player.getWorld() != null;
        thisMap = Bedwars.getMapFromMaps(player.getWorld());

        if (thisMap != null) {
            thisMap.getResourceTimer().setStore(store);
            thisMap.getResourceTimer().setSamplePlayer(player);
            if (thisMap.isActivated()) {
                queueController = thisMap.getQueueController();

                if (!thisMap.gameCommenced()) {
                    BedwarsMap thisMap = Bedwars.getMapFromMaps(player.getWorld());
                    Vector3d queueSpawn = thisMap.getQueueSpawn();

                    Transform whereTo = new Transform(queueSpawn, new Vector3f(0, 0, 0));


                    queueController.addPlayer(player);

                    assert ref != null : "playerRef null when trying to read their join info.";
                    assert thisMap != null : "This world is not in the list of maps!!";

                    // TELEPORT PLAYER UPON JOIN TO QUEUE SPAWN LOCATION.
                    World world = thisMap.getWorld();
                    Bedwars.teleportTo(thisMap.getPlayerManager().get(player), world, ref, queueSpawn, new Vector3f(), world.getName());

                } else {
                    if (thisMap.getPlayerManager().contains(ref)) {
                        if (thisMap.getPlayerManager().get(ref).canRejoin()) {
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
        } else {
            if (player.hasPermission("bedwars.op")) { // TODO: Add another check for if the world is a server lobby (Not until way later).
                BedwarsMessenger.promptUserToDeploy(player);
            }
        }
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

        // Null/Invalidity Checks
        assert player != null;
        assert player.getWorld() != null;
        thisMap = Bedwars.getMapFromMaps(player.getWorld());
        assert thisMap != null : "Failed to remove player from player manager, the BedwarsMap registered as null when the player left.";
        assert thisMap.getPlayerManager() != null;

        if (thisMap.isActivated() && !thisMap.gameCommenced() && (thisMap.getPlayerManager().contains(ref) || thisMap.getPlayerManager().contains(player))) {
                thisMap.getPlayerManager().remove(player);
        }

        // Execute during game queue
        // Player will leave the lobby and won't be able to rejoin the same world once its game has started.

        // Execute when leaving during game
        // Having stayed through the entire queue, the player is eligible to rejoin, and will be deemed dead/eliminated depending on the bed's state.
    }
}
