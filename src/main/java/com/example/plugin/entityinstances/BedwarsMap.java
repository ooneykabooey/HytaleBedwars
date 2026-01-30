package com.example.plugin.entityinstances;

import com.example.plugin.Bedwars;
import com.example.plugin.controllers.BedwarsInGameQueueController;
import com.example.plugin.managers.BedwarsPlayerManager;
import com.example.plugin.managers.BedwarsTeamsManager;
import com.example.plugin.managers.BedwarsItemTimerManager;
import com.example.plugin.utils.BedwarsLogger;
import com.example.plugin.utils.GAMEMODE;
import com.example.plugin.utils.TeamColor;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.*;


///  @author ooney
public class BedwarsMap {

    private boolean activated = false;
    private Bedwars plugin;
    private boolean commenced;
    private World world;
    private Vector3d queueSpawn;
    private ArrayList<BedwarsMidResource> midResources; // Resources at mid (Diamond, Emerald)
    private GAMEMODE gamemode;
    private BedwarsItemTimerManager resourceTimer = new BedwarsItemTimerManager(this);
    private BedwarsPlayerManager playerManager = new BedwarsPlayerManager();
    private BedwarsTeamsManager teamsManager = new BedwarsTeamsManager(playerManager, this);
    private BedwarsInGameQueueController queueController = new BedwarsInGameQueueController(this);
    private Set<Vector3i> blocksPlaced = new HashSet<>();


    public static final Map<String, GAMEMODE> possibleGamemodes = Map.of(
            "Solos (1v1s)", GAMEMODE.ONES,
            "Doubles (2v2s)", GAMEMODE.TWOS,
            "Trios (3v3s)", GAMEMODE.THREES,
            "Squads (4v4s)", GAMEMODE.FOURS,
            "Squad v Squad (4v4)", GAMEMODE.FOURAFOUR
    );

    // Get which teams are valid for the selected gamemode
    public static final Map<GAMEMODE, List<TeamColor>> teamsPerGamemode =
            Map.of(
                    GAMEMODE.ONES, List.of(TeamColor.values()),
                    GAMEMODE.TWOS, List.of(TeamColor.values()),
                    GAMEMODE.THREES, List.of(TeamColor.values()),
                    GAMEMODE.FOURS, List.of(TeamColor.values()),
                    GAMEMODE.FOURAFOUR, List.of(TeamColor.RED, TeamColor.BLUE)
            );

    // Set as null or default at first, the manager will run checks and the user will do setters on deployment.
    // This is so that the UI can throw errors upon deployment.
    public BedwarsMap() {
        queueSpawn = new Vector3d();
        midResources = new ArrayList<>();
        gamemode = null;
    }

    public BedwarsMap(World world) {
        this();
        if (world != null) {
            this.world = world;
        } else {
            throw new NullPointerException("World null when attempting to register a BedwarsMap.");
        }
    }

    /** Setter for the queueSpawn Vector3d, reads in doubles directly instead of a Vector3d.
     *
     * @param x SE
     * @param y SE
     * @param z SE
     */
    public void registerMapsQueueSpawn(double x, double y, double z) {
        queueSpawn.setX(x); queueSpawn.setY(y); queueSpawn.setZ(z);
    }


    ///  REGISTER SETTERS

    /** Add a mid resource into the map.
     *
     * @param resource
     */
    public void registerMapsMidResource(BedwarsMidResource resource) {
        midResources.add(resource);
    }

    /** Add a team to the map.
     *
     */
    public void registerTeamToMap(BedwarsTeam team) {
        teamsManager.addToTeam(team);
    }


    ///  REGISTER REMOVERS

    public void removeMapsMidResource(BedwarsMidResource resource) {
        midResources.remove(resource);
    }

    public void removeMapsMidResource(int index) {
        midResources.remove(index);
    }

    public void removeTeamFromMap(BedwarsTeam team) {
        teamsManager.removeTeam(team);
    }



    ///  GETTERS  ///

    public BedwarsTeam getTeam(BedwarsTeam team) {
        return teamsManager.getTeam(team);
    }

    public ArrayList<BedwarsTeam> getTeams() {
        return teamsManager.getTeams();
    }

    public BedwarsMidResource getMidResource(BedwarsMidResource resource) {
        for (BedwarsMidResource r : midResources) {
            if (r.equals(resource)) {
                return r;
            }
        }
        return null;
    }

    public Collection<BedwarsMidResource> getMidResources() {
        return midResources;
    }

    ///  GET/SET WORLD

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /// GET/SET GAMEMODE

    public GAMEMODE getGamemode() {
        return gamemode;
    }

    public void setGamemodeFromString(String s) {
        if (!possibleGamemodes.containsKey(s)) {
            throw new NoSuchElementException("There is no mapping that \"" + s + "\" covers.");
        }
        gamemode = possibleGamemodes.get(s);
    }

    public void setGamemode(GAMEMODE gamemode) {
        this.gamemode = gamemode;
    }

    /// GET/SET QUEUE SPAWN

    public Vector3d getQueueSpawn() {
        return queueSpawn;
    }

    public void setQueueSpawn(Vector3d queueSpawn) {
        this.queueSpawn = queueSpawn;
    }

    /** UPDATE MAP
     *
     *
     *
     *
     */
    public void updateMap(Vector3i anyBlockBroken, BlockType blockType, BedwarsPlayer perpetrator) {
        this.teamsManager.updateTeams(anyBlockBroken, blockType, perpetrator);
    }

    /// SET/GET TEAM MANAGER

    public BedwarsTeamsManager getTeamsManager() {
        return teamsManager;
    }

    public void setTeamsManager(BedwarsTeamsManager teamsManager) {
        this.teamsManager = teamsManager;
    }

    /// SET/GET ACTIVITY

    public boolean gameCommenced() {
        return commenced;
    }

    public void setCommenced(boolean active) {
        this.commenced = active;
    }

    public boolean isActivated() {return activated;}

    public void setActivated(boolean value) {this.activated = value;}

    public void setPlugin(Bedwars bedwars) {
        this.plugin = bedwars;
    }

    public Bedwars getPlugin() {
        return plugin;
    }

    public void addToResourceTimer(BedwarsItemTimer timer) {
        resourceTimer.addTimer(timer);
    }

    public void addAllToResourceTimer(Collection<BedwarsItemTimer> timers) {
        resourceTimer.addTimers(timers);
    }

    public BedwarsItemTimerManager getResourceTimer() {
        return resourceTimer;
    }


    public void addTeam(BedwarsTeam team) {
        this.teamsManager.addToTeam(team);
    }

    ///  PLAYER MANAGER

    public BedwarsPlayerManager getPlayerManager() {
        return playerManager;
    }

    public BedwarsInGameQueueController getQueueController() {
        return queueController;
    }

    public Set<Vector3i> getBlocksPlaced() {
        return blocksPlaced;
    }

    public void addBlockPlaced(Vector3i block) {
        blocksPlaced.add(block);
    }

    public boolean isBlockPlaced(Vector3i block) {
        return blocksPlaced.contains(block);
    }

    public void removeBlockPlaced(Vector3i block) {
        blocksPlaced.remove(block);
    }


    ///  ------------------- ///
    /// ----- END GAME ------ ///
    ///  ------------------- ///

    public void endGame() {

        /// CLEAR ALL PLACED BLOCKS FROM MAP
        for (Vector3i block : blocksPlaced) {
            world.setBlock(block.x, block.y, block.z, BlockType.EMPTY_KEY);
        }

        /// CLEAR ALL PLAYERS FROM TEAMS, RESET ALL BEDS
        for (BedwarsTeam team : teamsManager.getTeams()) {
            
            team.clearPlayerList();
            Vector3i bedLocation = team.getBedLocation().toVector3i();
            
            if (world.getBlockType(bedLocation).equals(BlockType.EMPTY)) {
                world.setBlock(bedLocation.x, bedLocation.y, bedLocation.z, "Furniture_Crude_Bed");
            }

            team.clearPlayerList();
        }

        /// REMOVE ALL PLAYERS FROM WORLD
        if (!playerManager.getAll().isEmpty()) {
            for (BedwarsPlayer player : playerManager.getAll()) {
                // Temporary measure, refer to the server lobby once it gets implemented.
                if (player.isEliminated()) {
                    player.kick("Good Game! You'll get'em next time :)");
                } else {
                    player.kick("Good Game! Amazing win!!");
                }

                playerManager.remove(player);
            }
        }

        // STOP ALL RESOURCES SPAWNING
        resourceTimer.stop();

        /// KILL ALL ITEMS SPAWNED, I CREDIT CLEARITY PLUGIN FOR THIS.
        if (world.isAlive()) {
            world.execute(() -> {

                Store<EntityStore> store = world.getEntityStore().getStore();
                ComponentType<EntityStore, ItemComponent> itemType = ItemComponent.getComponentType();
                Query<EntityStore> query = Query.and(itemType);

                int[] removed = new int[]{0};

                store.forEachChunk(query, (chunk, buffer) -> {
                    int size = chunk.size();

                    for (int i = 0; i < size; ++i) {
                        Ref<EntityStore> ref = chunk.getReferenceTo(i);
                        int x = removed[0]++;
                        buffer.tryRemoveEntity(ref, RemoveReason.REMOVE);
                    }

                });

            });

        }


        // Reset all in-game timers (sudden death/resource ramp ups)
        // Reset all team upgrades

        BedwarsLogger.logEndGame(world);
        commenced = false;
    }

}
