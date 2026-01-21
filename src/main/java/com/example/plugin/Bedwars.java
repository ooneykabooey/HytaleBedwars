package com.example.plugin;

import com.example.plugin.commands.DebugCommand;
import com.example.plugin.commands.DeployCommand;
import com.example.plugin.commands.TestUICommand;
import com.example.plugin.controllers.BedwarsInGameQueueController;
import com.example.plugin.events.BlockBreakSystem;
import com.example.plugin.events.BlockPlaceSystem;
import com.example.plugin.listeners.PlayerJoinLeaveSystem;
import com.example.plugin.listeners.PlayerJoinLeaveSystem;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.utils.BedwarsItemTimerManager;
import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/// @author ooney, yasha

///  MAIN PLUGIN BRANCH.
public class Bedwars extends JavaPlugin {

    // TODO: Review all project warnings and see of any resolutions to them.

    private boolean debug = false;
    private BedwarsMessenger messenger = new BedwarsMessenger(this);

    private boolean started = false;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private BedwarsItemTimerManager resourceTimer = new BedwarsItemTimerManager(this);
    private BedwarsInGameQueueController queueController = new BedwarsInGameQueueController(this);

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

        // Register EntityStore Systems
        estorereg.registerSystem(new BlockBreakSystem(this));
        estorereg.registerSystem(new BlockPlaceSystem());
        estorereg.registerSystem(new PlayerJoinLeaveSystem(this));

        this.getCommandRegistry().registerCommand(new DeployCommand(this));
        this.getCommandRegistry().registerCommand(new TestUICommand());
        this.getCommandRegistry().registerCommand(new DebugCommand(this));


//        resourceTimer.start();

    }

    // Helper method to register all commands inside the plugin, for readability's sake.
    private void registerCommands() {

    }

    public boolean debugMode() {
        return debug;
    }
    public void setDebug(boolean value) {this.debug = debug;}

    public boolean gameCommenced() {
        return started;
    }

    public BedwarsItemTimerManager getResourceTimer() {
        return resourceTimer;
    }

    ///  DEPLOY ///

    public static void deploy(GAMEMODE gamemode) {

    }


}
