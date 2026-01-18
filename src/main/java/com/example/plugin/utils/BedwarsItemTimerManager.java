package com.example.plugin.utils;

import com.example.plugin.Bedwars;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.SwitchActiveSlotEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BedwarsItemTimerManager {

    // TODO: Get references to all of the players, and the coordinate vectors to all team resource positions (3.5 blocks from their spawnpoint).

    private Bedwars plugin;
    private boolean started = false;


    public BedwarsItemTimerManager(Bedwars plugin) {
        this.plugin = plugin;
    }

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // List of items to spawn with their respective rates.
    private final List<BedwarsItemTimer> times = List.of(
            new BedwarsItemTimer("LIFE",1, new BedwarsItemTimer.DropEntry("Ingredient_Life_Essence", 1)), // Life
            new BedwarsItemTimer("ICE",8, new BedwarsItemTimer.DropEntry("Ingredient_Ice_Essence", 1)), // Ice
            new BedwarsItemTimer("FIRE",45,new BedwarsItemTimer.DropEntry("Ingredient_Fire_Essence", 1)), // Fire
            new BedwarsItemTimer("VOID",60,new BedwarsItemTimer.DropEntry("Ingredient_Void_Essence", 1)) // Void
    );

    // This will be for spawning items for every team and every mid position.
    private final List<List<BedwarsItemTimer>> timesoftimes = List.of();

    private int count;

    /** Start the item spawning, activated upon blocks being damaged and only after the game has started.
            ticks every second, repeats forever until stopped.
     */
    public void start(Store<EntityStore> store, Player player) {
        started = true;

        EventTitleUtil.showEventTitleToUniverse(Message.raw("Ticking :D"), Message.raw("Timer commenced."), true, null, 5, 1, 1);
            scheduler.scheduleAtFixedRate(() -> {
                for (BedwarsItemTimer timer : times) {
                    if (plugin.gameCommenced() && started) {
                        timer.tick(store, player);
                    }
                }
                if (plugin.debugMode()) {
                    count++;
                    if (count % 10 == 0) {
                        EventTitleUtil.showEventTitleToUniverse(Message.raw("Tick"), Message.raw("Tock"), true, null, 100, 1,0);
                    }
                }
            },0,1, TimeUnit.SECONDS);

    }

    /**
     * Stops the timers.
     */
    public void stop() {
        scheduler.shutdown();
    }

    // Accessor method for started flag.
    public boolean started() {
        return started;
    }
}