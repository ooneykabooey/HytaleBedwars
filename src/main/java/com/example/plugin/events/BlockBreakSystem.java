package com.example.plugin.events;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsPlayer;
import com.example.plugin.managers.BedwarsPlayerManager;
import com.example.plugin.messenger.BedwarsMessenger;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/// @author ooney

public class BlockBreakSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {

    Bedwars plugin;
    BedwarsMap thisMap;

    public BlockBreakSystem(Bedwars plugin) {
        super(BreakBlockEvent.class);
        this.plugin = plugin;
    }

    @Override
    public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull BreakBlockEvent damageBlockEvent) {

        if (damageBlockEvent.getBlockType() != BlockType.EMPTY) {

            BedwarsPlayer perpetrator;
            Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);

            Player player = store.getComponent(ref, Player.getComponentType());
            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());

            if (player == null) return;
            if (playerRef == null) return;

            BedwarsMessenger.playerDamagedBlockMessage(player);

            World world = player.getWorld();
            if (world == null) return;


            if (thisMap == null) {
                thisMap = Bedwars.getMapFromMaps(world);
            }

            if (thisMap == null) {
                    if (!player.getGameMode().equals(GameMode.Creative)) {
                        damageBlockEvent.setCancelled(true);
                    }
            } else {
                BedwarsPlayerManager playerManager = thisMap.getPlayerManager();

                if (playerManager != null) {

                    perpetrator = playerManager.get(player);

                    Vector3i targetBlock = damageBlockEvent.getTargetBlock();


                    if (!(player.getGameMode() == GameMode.Creative) && !(thisMap.isBlockPlaced(targetBlock)) && !(damageBlockEvent.getBlockType().getId().equals("Furniture_Crude_Bed"))) {
                        damageBlockEvent.setCancelled(true);
                        BedwarsMessenger.notAllowedToBreakMapMessage(player);
                    }

                    if (damageBlockEvent.getBlockType().getId().equals("Furniture_Crude_Bed")) {

                        assert thisMap != null;

                        if (!(thisMap.getPlayerManager().get(player).getTeam() == null) && !thisMap.getPlayerManager().get(player).getTeam().getBedLocation().equals(damageBlockEvent.getTargetBlock().toVector3d())) {
                            thisMap.updateMap(damageBlockEvent.getTargetBlock(), damageBlockEvent.getBlockType(), perpetrator);
                        } else if (thisMap.getPlayerManager().get(player).getTeam() == null) { // Hacked out of queue to break bed. Not supposed to be ingame.
                            Universe.get().removePlayer(playerRef);
                        } else {
                            damageBlockEvent.setCancelled(true);
                            BedwarsMessenger.cannotDestroyOwnBed(player);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }

}
