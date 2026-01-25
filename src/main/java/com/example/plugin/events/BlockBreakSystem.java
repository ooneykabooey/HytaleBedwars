package com.example.plugin.events;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.managers.BedwarsTeamsManager;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.utils.BedwarsItemTimerManager;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.spawn.ISpawnProvider;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/// @author ooney

public class BlockBreakSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {

    Bedwars plugin;
    public static Set<Vector3i> blocksPlaced = new HashSet<>();
    BedwarsMap thisMap;

    public BlockBreakSystem(Bedwars plugin) {
        super(BreakBlockEvent.class);
        this.plugin = plugin;
    }

    @Override
    public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull BreakBlockEvent damageBlockEvent) {

        if (damageBlockEvent.getBlockType() != BlockType.EMPTY) {



            Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);

            Player player = store.getComponent(ref, Player.getComponentType());
            if (player == null) return;

            if (thisMap == null) {
                thisMap = Bedwars.getMapFromMaps(player.getWorld());
            }


            BedwarsMessenger.playerDamagedBlockMessage(player);


            World world = player.getWorld();
            if (world == null) return;
//
//            assert player.getReference() != null;
//
//            PlayerRef playerRef = (PlayerRef) player.getReference().getStore().getComponent(ref, PlayerRef.getComponentType());
//            assert playerRef != null;

            Vector3i targetBlock = damageBlockEvent.getTargetBlock();
//            Vector3d blockPos = targetBlock.toVector3d();

            if (!(player.getGameMode() == GameMode.Creative) && !(blocksPlaced.contains(targetBlock)) && !(damageBlockEvent.getBlockType().getId().equals("Furniture_Crude_Bed"))) {
                damageBlockEvent.setCancelled(true);
                BedwarsMessenger.notAllowedToBreakMapMessage(player);
            }

            if (damageBlockEvent.getBlockType().getId().equals("Furniture_Crude_Bed")) {
                if (thisMap != null) {
                    thisMap.updateMap(damageBlockEvent.getTargetBlock());
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
