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
    // TODO: Make players an ArrayList and refactor where its used, HashMaps are O(n) and not stable.
    private ArrayList<BedwarsPlayer> players = new ArrayList<>();

    // private ArrayList<BedwarsPlayer> players = new ArrayList<>();


    /**
     * Add a player to the manager.
     *
     * @param player the Player object
     */
    public void add(Player player) {
        players.add(new BedwarsPlayer(player));

    }

    /**
     * Takes in a Player class to remove BedwarsPlayer
     *
     */
    public void remove(Player p) {
        for (BedwarsPlayer player : players) {
            if (player.getPlayer().equals(p)) {
                players.remove(player);
                return;
            }
        }
    }

    /**
     * Takes in player ref to remove BedwarsPlayer.
     * @param playerRef
     */
    public void remove(Ref<EntityStore> playerRef) {
        for (BedwarsPlayer player : players) {
            if (player.getRef().equals(playerRef)) {
                players.remove(player);
            }
        }
    }

    public void remove(BedwarsPlayer player) {
        players.remove(player);
    }

    /**
     * Get the BedWars player object for the given entity ref.
     *
     * @param ref the player's entity reference
     * @return the BedwarsPlayer object, or null if not found
     */
    public BedwarsPlayer get(Ref<EntityStore> ref) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getRef().equals(ref)) {
                return players.get(i);
            }
        }
        return null;
    }

    public BedwarsPlayer get(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayer().equals(player)) {
                return players.get(i);
            }
        }
        return null;
    }

    public BedwarsPlayer get(BedwarsPlayer player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player)) {
                return players.get(i);
            }
        }
        return null;
    }

    public BedwarsPlayer get(int index) {
        return players.get(index);
    }


    /**
     * Check if a player is registered.
     */
    public boolean contains(BedwarsPlayer player) {
        return players.contains(player);
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public boolean contains(Ref<EntityStore> player) {
        return players.contains(player);
    }

    /**
     * Get all registered BedWars players.
     */
    public Collection<BedwarsPlayer> getAll() {
        return players;
    }

    /**
     * @return Get amount of players in the list.
     */
    public int getSize() {return players.size();}


}
