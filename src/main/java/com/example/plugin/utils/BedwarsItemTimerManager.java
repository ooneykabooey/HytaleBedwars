package com.example.plugin.utils;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.messenger.BedwarsMessenger;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.SwitchActiveSlotEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/// @author ooney

public class BedwarsItemTimerManager {

    // TODO: Get references to all of the players, and the coordinate vectors to all team resource positions (3.5 blocks from their spawnpoint).

    private boolean started = false;
    private BedwarsMap thisMap;
    private Store<EntityStore> store;
    private Player samplePlayer;

    public BedwarsItemTimerManager(BedwarsMap map) {
        thisMap = map;
    }

    private ScheduledFuture<?> scheduler;

    // List of items to spawn with their respective rates.
    // CopyOnWriteArrayList for thread safety without locking, since it checks the list every second its ticking.
    private final List<BedwarsItemTimer> times = new CopyOnWriteArrayList<>();

    /** Start the item spawning, activated upon blocks being damaged and only after the game has started.
            ticks every second, repeats forever until stopped.
     */
    public void start(Store<EntityStore> store, Player player) {
        started = true;

            scheduler = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
                for (BedwarsItemTimer timer : times) {


                    if (timer.getThisTeam() == null) { // if no team (probably a mid resource), still generate.
                        timer.tick(store, player, timer.getLocation());
                    } else if (!(timer.getThisTeam().getPlayers().isEmpty())) { // if the team is not empty, generate. TODO: Fix this.
                        timer.tick(store, player, timer.getLocation());
                    }



                }
            },0,1, TimeUnit.SECONDS);

    }

    /**
     * Stops the timers.
     */
    public @Nullable CompletableFuture<Void> stop() {
        if (scheduler != null) {
            scheduler.cancel(false);
            scheduler = null;
        }
        return CompletableFuture.completedFuture(null);
    }

    // Accessor method for started flag.
    public boolean started() {
        return started;
    }

    /// GETTER/SETTER TIMES

    public BedwarsItemTimer getTimerFromTimer(BedwarsItemTimer timer) {
        for (BedwarsItemTimer time : times) {
            if (time.equals(timer)) {
                return time;
            }
        }
        return null;
    }

    public BedwarsItemTimer getTimerFromIndex(int i) {
        return this.times.get(i);
    }

    public void setTimerAt(int i, BedwarsItemTimer timer) {
        this.times.set(i, timer);
    }

    public void addTimer(BedwarsItemTimer timer) {
        this.times.add(timer);
    }

    public void addTimers(Collection<BedwarsItemTimer> collection) {
            times.addAll(collection);
    }


    public void setStore(Store<EntityStore> store) {
        this.store = store;
    }

    public Store<EntityStore> getStore() {
        return store;
    }

    public void setSamplePlayer(Player player) {
        this.samplePlayer = player;
    }

    public Player getSamplePlayer() {
        return samplePlayer;
    }


}
