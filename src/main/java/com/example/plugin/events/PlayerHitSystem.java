package com.example.plugin.events;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsPlayer;
import com.example.plugin.managers.BedwarsPlayerManager;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerHitSystem extends EntityEventSystem<EntityStore, Damage> {

    public PlayerHitSystem() {
        super(Damage.class);
    }

    @Override
    public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull Damage event) {
        Ref<EntityStore> victimRef = archetypeChunk.getReferenceTo(i);
        Player victim = store.getComponent(victimRef, Player.getComponentType());

        if (victim == null) return;         // Check 1, player null
        if (!(event.getSource() instanceof Damage.EntitySource)) return;         // Check 2, damage source not EntitySource.

        Ref<EntityStore> attackerRef = ((Damage.EntitySource) event.getSource()).getRef();
        if (!attackerRef.isValid()) return;         // Check 3, attacker's reference not valid

        Player attacker = attackerRef.getStore().getComponent(attackerRef, Player.getComponentType());
        if (attacker == null) return;        // Check 4, attacker's player is null.

        BedwarsMap map = Bedwars.getMapFromMaps(victim.getWorld());
        if (map == null) return;        // Check 5, BedwarsMap is null.

        if (!map.gameCommenced()) { // Check 6, Cancel event if the game hasn't commenced.
            event.setCancelled(true);
            return;
        }

        BedwarsPlayerManager playerManager = map.getPlayerManager();

        if (playerManager == null) {         // Check 7, Cancel event if the playermanager is null.
            event.setCancelled(true);
            return;
        }

        BedwarsPlayer bedwarsVictim = playerManager.get(victim);
        BedwarsPlayer bedwarsAttacker = playerManager.get(attacker);

        if (bedwarsVictim == null || bedwarsAttacker == null) return; // Check 8, both victim and attacker aren't BedwarsPlayers

        if (bedwarsVictim.getTeam() != null && bedwarsVictim.getTeam().equals(bedwarsAttacker.getTeam())) { // Check 9, cancel event if the two players are on the same team.
            event.setCancelled(true);
        }

    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
