package com.example.plugin.managers;

import com.example.plugin.entityinstances.BedwarsPlayer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


///  @author yasha, ooney

/**
 * Global registry for all BedWars players.
 * ECS-friendly: tracks players using Ref<EntityStore> as identity.
 */
public class BedwarsPlayerManager {

    // Ref<EntityStore> is the entity identity in ECS
    private static final Map<Ref<EntityStore>, BedwarsPlayer> players = new HashMap<>();
    private static final ArrayList<Ref<EntityStore>> indexOfPlayers = new ArrayList<>();

    /**
     * Add a player to the manager.
     *
     * @param ref    the player's entity reference
     * @param player the Player object
     */
    public static void add(Ref<EntityStore> ref, Player player) {
        players.put(ref, new BedwarsPlayer(ref, player));
        indexOfPlayers.add(ref);
    }

    /**
     * Remove a player from the manager.
     *
     * @param ref the player's entity reference
     */
    public static void remove(Ref<EntityStore> ref) {
        players.remove(ref);
        indexOfPlayers.remove(ref);
    }

    /**
     * Get the BedWars player object for the given entity ref.
     *
     * @param ref the player's entity reference
     * @return the BedwarsPlayer object, or null if not found
     */
    public static BedwarsPlayer get(Ref<EntityStore> ref) {
        return players.get(ref);
    }

    /**
     * Check if a player is registered.
     */
    public static boolean contains(Ref<EntityStore> ref) {
        return players.containsKey(ref);
    }

    /**
     * Get all registered BedWars players.
     */
    public static Collection<BedwarsPlayer> getAll() {
        return players.values();
    }

    /**
     * @return Get amount of players in the list.
     */
    public static int getSize() {return players.size();}

    /**
     * @return Get the indexOfPlayers arraylist.
     */
    public static ArrayList<Ref<EntityStore>> getIndexOfPlayers() {return indexOfPlayers;}

    /** Get a player from an index of keys.
     *
     * @param i Index of the arraylist to search.
     * @return The player at that key.
     */
    public static BedwarsPlayer getPlayerFromIndex(int i) {return players.get(indexOfPlayers.get(i));}
}
