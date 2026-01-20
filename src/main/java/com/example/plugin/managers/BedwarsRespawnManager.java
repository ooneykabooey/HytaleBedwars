package com.example.plugin.managers;

import com.example.plugin.messenger.BedwarsMessenger;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.RespawnSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class BedwarsRespawnManager extends RespawnSystems.OnRespawnSystem {

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref<EntityStore> ref,
                                   @Nonnull DeathComponent component,
                                   @Nonnull Store<EntityStore> store,
                                   @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        // Player just respawned

        Player player = store.getComponent(ref, Player.getComponentType());

        // Give starter items (add to hotbar first, then storage)[^2]
        player.getInventory().getCombinedHotbarFirst().addItemStack(new ItemStack("Tool_Sword_Wood", 1));
        player.getInventory().getCombinedHotbarFirst().addItemStack(new ItemStack("Consumable_Apple", 5));

        // Send welcome back message
        BedwarsMessenger.respawnMessage(player);
    }
}