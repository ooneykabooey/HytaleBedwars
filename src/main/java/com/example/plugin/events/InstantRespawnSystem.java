package com.example.plugin.events;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsPlayer;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/// ORIGINAL AUTHOR: ender_griefeur99
/// Incorporated by ooney
public class InstantRespawnSystem extends DeathSystems.OnDeathSystem {

    private BedwarsMap thisMap;

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent deathComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        PlayerRef victimPlayerRef = (PlayerRef) commandBuffer.getComponent(ref, PlayerRef.getComponentType());
        Player player  = (Player) commandBuffer.getComponent(ref, Player.getComponentType());
       assert victimPlayerRef != null;

       if (thisMap == null) {
           thisMap = Bedwars.getMapFromMaps(player.getWorld());
       }
       
       EntityStore entityStore = (EntityStore) commandBuffer.getExternalData();
       World world = entityStore.getWorld();
       world.execute(() -> {
           Ref<EntityStore> playerEntityRef = victimPlayerRef.getReference();
           if (playerEntityRef != null && playerEntityRef.isValid()) {
               Store<EntityStore> liveStore = playerEntityRef.getStore();
               Player playerComponent = (Player) liveStore.getComponent(playerEntityRef, Player.getComponentType());
               if (playerComponent != null) {
                   BedwarsPlayer bedwarsPlayer = thisMap.getPlayerManager().get(playerComponent);
                   assert bedwarsPlayer.getTeam() != null : "Bedwars player's team is null, this is triggered most likely by the user voiding during queue.";
                   playerComponent.getPageManager().setPage(playerEntityRef, liveStore, Page.None);
                   if (bedwarsPlayer.getTeam().hasBed()) {
                       Bedwars.teleportTo(thisMap.getPlayerManager().get(playerComponent), thisMap.getWorld(), ref, bedwarsPlayer.getTeam().getSpawnLocation(), new Vector3f(), world.getName());
                   } else {
                       Bedwars.teleportTo(thisMap.getPlayerManager().get(playerComponent), thisMap.getWorld(), ref, thisMap.getQueueSpawn(), new Vector3f(), world.getName());
                   }
               }
           }
       });
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }
}
