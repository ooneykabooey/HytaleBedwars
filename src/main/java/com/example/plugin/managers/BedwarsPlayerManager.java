package com.example.plugin.managers;

import com.example.plugin.entityinstances.BedwarsPlayer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


///  @author yasha, ooney

/**
 * Global registry for all BedWars players.
 * ECS-friendly: tracks players using Ref<EntityStore> as identity.
 */
public class BedwarsPlayerManager {

    // Ref<EntityStore> is the entity identity in ECS. Using ConcurrentHashMap for thread-safety and O(1) lookups.
    private final Map<Ref<EntityStore>, BedwarsPlayer> players = new ConcurrentHashMap<>();

    public void startGameGiveKit() {
        for (BedwarsPlayer player : players.values()) {
            player.getPlayer().getInventory().getCombinedHotbarFirst().addItemStack(new ItemStack("Weapon_Sword_Wood", 1));
            player.getPlayer().getInventory().getCombinedHotbarFirst().addItemStack(new ItemStack("Cloth_Block_Wool_" + player.getTeam().getId(), 100));
        }
    }


    /**
     * Add a player to the manager.
     *
     * @param player the Player object
     */
    public void add(Player player) {
        if (player == null) return;
        BedwarsPlayer bedwarsPlayer = new BedwarsPlayer(player);
        players.put(bedwarsPlayer.getRef(), bedwarsPlayer);
    }

    /**
     * Takes in a Player class to remove BedwarsPlayer
     *
     */
    public void remove(Player p) {
        if (p == null) return;
        players.remove(p.getReference());
    }

    /**
     * Takes in player ref to remove BedwarsPlayer.
     * @param playerRef
     */
    public void remove(Ref<EntityStore> playerRef) {
        if (playerRef == null) return;
        players.remove(playerRef);
    }

    public void remove(BedwarsPlayer player) {
        if (player == null) return;
        players.remove(player.getRef());
    }

    /**
     * Get the BedWars player object for the given entity ref.
     *
     * @param ref the player's entity reference
     * @return the BedwarsPlayer object, or null if not found
     */
    public BedwarsPlayer get(Ref<EntityStore> ref) {
        return players.get(ref);
    }

    public BedwarsPlayer get(Player player) {
        if (player == null) return null;
        return players.get(player.getReference());
    }

    public BedwarsPlayer get(BedwarsPlayer player) {
        if (player == null) return null;
        return players.get(player.getRef());
    }

    /**
     * Check if a player is registered.
     */
    public boolean contains(BedwarsPlayer player) {
        if (player == null) return false;
        return players.containsKey(player.getRef());
    }

    public boolean contains(Player player) {
        if (player == null) return false;
        return players.containsKey(player.getReference());
    }

    public boolean contains(Ref<EntityStore> playerRef) {
        return players.containsKey(playerRef);
    }

    /**
     * Get all registered BedWars players.
     */
    public Collection<BedwarsPlayer> getAll() {
        return players.values();
    }

    /**
     * @return Get amount of players in the list.
     */
    public int getSize() {return players.size();}


}
