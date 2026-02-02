package com.example.plugin.entityinstances;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

/// @author ooney

public class BedwarsItemTimer {

    private int start; // Amount of time to restart with.
    private int current; // Current progress
//    private boolean ticking;
    private String ID; // ID of the type of spawner.
    private DropEntry dropEntry; // DropEntry for getting the item it drops and amount.
    //private EntityStore store; // EntityStore gathered from event, used to reference player and position.
    private Vector3d location;
    private BedwarsMap thisMap;
    private BedwarsTeam thisTeam;
    private boolean isUpgrade;

    public BedwarsItemTimer(String ID, int seconds, DropEntry dropEntry, Vector3d location, BedwarsMap map, BedwarsTeam team, boolean isUpgrade)  {
        this.ID = ID;
        this.start = seconds;
        this.current = seconds;
        this.dropEntry = dropEntry;
        //this.store = map.getWorld().getEntityStore();
        this.location = location;
        thisMap = map;
        thisTeam = team;
        this.isUpgrade = isUpgrade;
    }

    /** tick the timer by one unit. Drop the item once completed.
     *
     */
    public void tick(Store<EntityStore> store, Player player, Vector3d dropVector) {

            current--;
            if (current < 0) {
                Item item = Item.getAssetMap().getAsset(dropEntry.getItem());
                if (item != null) {
                    ItemStack itemStack = new ItemStack(item.getId(), dropEntry.getAmount());
                    Holder<EntityStore>[] itemEntityHolders = ItemComponent.generateItemDrops(store, List.of(itemStack), dropVector, Vector3f.ZERO);
                    player.getWorld().execute(() -> store.addEntities(itemEntityHolders,AddReason.SPAWN));
                }
                current = start;
            }

    }

//    public void stopTicking() {
//        ticking = false;
//    }
//
//    public void startTicking() {
//        ticking = true;
//    }

//    public boolean isTicking() {
//        return ticking;
//    }

    public static class DropEntry {
        private String item = "";
        private int amount = 1;

        // Null constructor
        public DropEntry() {
        }

        public DropEntry(String item, int amount) {
            this.item = item;
            this.amount = amount;
        }

        public String getItem() { return this.item; }

        public int getAmount() { return this.amount; }

    }

    ///  ACCESSORS

    public String getID() {
        return ID;
    }

    public DropEntry getDropEntry() {
        return dropEntry;
    }

    public Vector3d getLocation() {
        return location;
    }

    public BedwarsMap getThisMap() {
        return thisMap;
    }

    public BedwarsTeam getThisTeam() {
        return thisTeam;
    }

    /// MUTATORS

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setDropEntry(DropEntry dropEntry) {
        this.dropEntry = dropEntry;
    }

    public void setLocation(Vector3d location) {
        this.location = location;
    }

    public void setLocation(double x, double y, double z) {
        this.location = new Vector3d(x, y, z);
    }

    public void setThisMap(BedwarsMap map) {
        thisMap = map;
    }

    public void setThisTeam(BedwarsTeam team) {
        thisTeam = team;
    }



}
