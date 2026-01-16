package com.example.plugin.managers;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.RespawnSystems;

import javax.annotation.Nonnull;

public class BedwarsRespawnManager extends RespawnSystems.OnRespawnSystem {

    @Nonnull
    @Override
    public Query getQuery() {
        return Player.getComponentType();
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref ref,
                                   @Nonnull DeathComponent component,
                                   @Nonnull Store store,
                                   @Nonnull CommandBuffer commandBuffer) {
        // Player just respawned

        Player player = (Player) store.getComponent(ref, Player.getComponentType());

        // Give starter items (add to hotbar first, then storage)[^2]
        player.getInventory().getCombinedHotbarFirst().addItemStack(new ItemStack("Tool_Sword_Wood", 1));
        player.getInventory().getCombinedHotbarFirst().addItemStack(new ItemStack("Consumable_Apple", 5));

        // Send welcome back message
        player.sendMessage(Message.raw("You have respawned!"));
    }
}