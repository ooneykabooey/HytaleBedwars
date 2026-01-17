package com.example.plugin;

import com.example.plugin.events.BlockBreakSystem;
import com.example.plugin.events.BlockPlaceSystem;
import com.example.plugin.listeners.PlayerJoinLeaveSystem;
import com.example.plugin.listeners.PlayerJoinLeaveSystem;
import com.example.plugin.utils.BedwarsItemTimerManager;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

///  MAIN PLUGIN BRANCH.
public class Bedwars extends JavaPlugin {

    private boolean debug = false;
    private boolean started = true;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private BedwarsItemTimerManager resourceTimer = new BedwarsItemTimerManager(this);

    // Metadata
    private final String pluginName = "Hytale Bedwars";
    private final String pluginVersion = "1.0.0";

    // Constructor
    public Bedwars(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    // Link classes to plugin and set up
    @Override
    protected void setup() {
        super.setup();
        ComponentRegistryProxy<EntityStore> estorereg = this.getEntityStoreRegistry();

        // Register System
        estorereg.registerSystem(new BlockBreakSystem(this));
        estorereg.registerSystem(new BlockPlaceSystem());

        // TODO: See about fragility.
        estorereg.registerSystem(new PlayerJoinLeaveSystem());


//        resourceTimer.start();

    }

    // Helper method to register all commands inside the plugin, for readability's sake.
    private void registerCommands() {
        // COMMANDS
    }

    public boolean debugMode() {
        return debug;
    }

    public boolean gameCommenced() {
        return started;
    }

    public BedwarsItemTimerManager getResourceTimer() {
        return resourceTimer;
    }


}
