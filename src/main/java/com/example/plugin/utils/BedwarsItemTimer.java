package com.example.plugin.utils;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public class BedwarsItemTimer {

    private int start; // Amount of time to restart with.
    private int current; // Current progress
//    private boolean ticking;
    private String ID; // ID of the type of spawner.
    private DropEntry dropEntry; // DropEntry for getting the item it drops and amount.
    private EntityStore store; // EntityStore gathered from event, used to reference player and position.

    public BedwarsItemTimer(String ID, int seconds, DropEntry dropEntry)  {
        this.ID = ID;
        this.start = seconds;
        this.current = seconds;
        this.dropEntry = dropEntry;
        this.store = store;
    }

    /** tick the timer by one unit. Drop the item once completed.
     *
     * @param store EntityStore taken from BedwarsItemTimerManager which is taken from BlockBreakSystem.
     * @param player Player reference to gather world data from.
     */
    public void tick(Store<EntityStore> store, Player player) {

            current--;
            if (current < 0) {
                Vector3d dropPos = new Vector3d(0.5, 200.0, 0.5);
                Item item = (Item) Item.getAssetMap().getAsset(dropEntry.getItem());
                if (item != null) {
                    ItemStack itemStack = new ItemStack(item.getId(), dropEntry.getAmount());
                    Holder<EntityStore>[] itemEntityHolders = ItemComponent.generateItemDrops(store, List.of(itemStack), dropPos, Vector3f.ZERO);
                    player.getWorld().execute(() -> player.getWorld().getEntityStore().getStore().addEntities(itemEntityHolders,AddReason.SPAWN));
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



}
