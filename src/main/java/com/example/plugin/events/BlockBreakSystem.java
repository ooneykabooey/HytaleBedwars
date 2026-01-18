package com.example.plugin.events;

import com.example.plugin.Bedwars;
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
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.spawn.ISpawnProvider;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockBreakSystem extends EntityEventSystem<EntityStore, DamageBlockEvent> {

    Bedwars plugin;
    public static Set<Vector3i> blocksPlaced = new HashSet<>();
    BedwarsItemTimerManager timerManager;

    public BlockBreakSystem(Bedwars plugin) {
        super(DamageBlockEvent.class);
        this.plugin = plugin;
        this.timerManager = plugin.getResourceTimer();
    }

    @Override
    public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull DamageBlockEvent damageBlockEvent) {

        // TODO: Check if the block that is broken is a bed.
        if (damageBlockEvent.getBlockType() != BlockType.EMPTY) {

            Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);

            Player player = store.getComponent(ref, Player.getComponentType());
            if (player == null) return;
            player.sendMessage(Message.raw("Damaged block!"));

            if (!timerManager.started()) {
                timerManager.start(store, player);
            }


            World world = player.getWorld();
            if (world == null) return;
//
//            assert player.getReference() != null;
//
//            PlayerRef playerRef = (PlayerRef) player.getReference().getStore().getComponent(ref, PlayerRef.getComponentType());
//            assert playerRef != null;

            Vector3i targetBlock = damageBlockEvent.getTargetBlock();
//            Vector3d blockPos = targetBlock.toVector3d();

            if (!(player.getGameMode() == GameMode.Creative) && !(blocksPlaced.contains(targetBlock))) {
                damageBlockEvent.setCancelled(true);
                player.sendMessage(Message.raw("You cannot break blocks that are apart of the map!"));
            }

        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
