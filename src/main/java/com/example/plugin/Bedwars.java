package com.example.plugin;
import com.example.plugin.commands.*;
import com.example.plugin.controllers.BedwarsInGameQueueController;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsPlayer;
import com.example.plugin.events.BlockBreakSystem;
import com.example.plugin.events.BlockPlaceSystem;
import com.example.plugin.events.EnderPearlSystem;
import com.example.plugin.events.InstantRespawnSystem;
import com.example.plugin.listeners.PlayerJoinLeaveSystem;
import com.example.plugin.managers.BedwarsMapManager;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.managers.BedwarsItemTimerManager;
import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.ModelTransform;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.packets.player.ClientTeleport;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/// @author ooney, yasha

///  MAIN PLUGIN BRANCH.
public class Bedwars extends JavaPlugin {

    // TODO: Review all project warnings and see of any resolutions to them.

    private boolean debug = false;
    private BedwarsMessenger messenger = new BedwarsMessenger(this);

    private boolean started = false;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final ArrayList<BedwarsMap> maps = new ArrayList<>();
    private BedwarsMapManager mapManager;
    private static Bedwars instance;


    // Metadata
    private final String pluginName = "Hytale Bedwars";
    private final String pluginVersion = "1.0.0";

    // Map Data


    // Constructor
    public Bedwars(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    // Link classes to plugin and set up
    @Override
    protected void setup() {
        super.setup();
        ComponentRegistryProxy<EntityStore> estorereg = this.getEntityStoreRegistry();

        // 1. Register Commands FIRST
        this.getCommandRegistry().registerCommand(new WelcomeUICommand());
        this.getCommandRegistry().registerCommand(new DebugCommand(this));
        this.getCommandRegistry().registerCommand(new ActivateMapCommand());
        this.getCommandRegistry().registerCommand(new BuyTestCommand());

        // Load the config
        this.mapManager = new BedwarsMapManager(this);

        // Load all maps found in the bedwars_maps folder
        try {
            mapManager.loadAllMaps();
        } catch (Exception e) {
            LOGGER.atInfo().log("Failed to load maps during setup", e);
        }
        
        // Register EntityStore Systems
        estorereg.registerSystem(new BlockBreakSystem(this));
        estorereg.registerSystem(new BlockPlaceSystem());
        estorereg.registerSystem(new PlayerJoinLeaveSystem(this));
        estorereg.registerSystem(new InstantRespawnSystem());
        estorereg.registerSystem(new EnderPearlSystem());
    }

    // Helper method to register all commands inside the plugin, for readability's sake.
    private void registerCommands() {

    }

    public static Bedwars getInstance() {
        return instance;
    }

    public BedwarsMapManager getMapManager() {
        return mapManager;
    }

    public boolean debugMode() {
        return debug;
    }
    public void setDebug(boolean value) {this.debug = debug;}

    public boolean gameCommenced() {
        return started;
    }

    public static BedwarsMap getMapFromMaps(World world) {
        for (BedwarsMap map : maps) {
            if (world.equals(map.getWorld())) {
                return map;
            }
        }
        return null;
    }

    public static void registerMap(BedwarsMap bedwarsMap) {
        maps.add(bedwarsMap);
    }

    public static void teleportTo(@Nonnull BedwarsPlayer player, @Nonnull World world, @Nonnull Ref<EntityStore> ref, @Nonnull Vector3d whereTo, @Nonnull Vector3f rotation, @Nonnull String worldName) {

        world.execute(() -> {
            Transform transform = new Transform(whereTo, rotation);

            if (ref.isValid() && ((EntityStore) ref.getStore().getExternalData()).getWorld().getName().equals(worldName)) {
                TransformComponent transformComponent = ref.getStore().getComponent(ref, TransformComponent.getComponentType());
                if (transformComponent != null) {
                    transformComponent.teleportPosition(whereTo);
                    ModelTransform modelTransform = new ModelTransform(new Position(whereTo.x, whereTo.y, whereTo.z), new Direction(rotation.x, rotation.y, rotation.z), new Direction(0,0,0));
                    assert player.getPlayerRef() != null && player.getPlayerRef().isValid() : "Player cannot be teleported: PlayerRef not valid or is null.";
                    player.getPlayerRef().getPacketHandler().writeNoCache(new ClientTeleport((byte) 0, modelTransform, true));
                }
            } else {
                // player.getPlayerRef().getReference() could possibly be replaced to player.getRef()
                Holder<EntityStore> holder = player.getPlayerRef().getReference() != null ? player.getPlayerRef().removeFromStore() : player.getPlayerRef().getHolder();
                if (holder != null) {
                    Universe.get().resetPlayer(player.getPlayerRef(), holder, world, transform);
                }
            }
        });
    }


}
