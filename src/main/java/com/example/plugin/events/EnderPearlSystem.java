package com.example.plugin.events;



import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.ProjectileComponent;
import com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.lang.reflect.Field;
import java.util.UUID;

///  @author Clark_KentHT with the EnderPoop mod.

public class EnderPearlSystem extends EntityTickingSystem<EntityStore> {
    public Query<EntityStore> getQuery() {
        return ProjectileComponent.getComponentType();
    }

    public void tick(float dt, int index, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        ProjectileComponent projectile = (ProjectileComponent)chunk.getComponent(index, ProjectileComponent.getComponentType());

        try {
            String assetName = projectile.getProjectileAssetName();
            if (assetName != null && !assetName.contains("Ingredient_Poop")) {
                return;
            }

            Object physicsProvider = projectile.getSimplePhysicsProvider();
            if (physicsProvider == null) {
                return;
            }

            Field haveHitField = ProjectileComponent.class.getDeclaredField("haveHit");
            haveHitField.setAccessible(true);
            boolean haveHit = haveHitField.getBoolean(projectile);
            Field onGroundField = physicsProvider.getClass().getDeclaredField("onGround");
            onGroundField.setAccessible(true);
            boolean physicsOnGround = onGroundField.getBoolean(physicsProvider);
            Field stateField = physicsProvider.getClass().getDeclaredField("state");
            stateField.setAccessible(true);
            Object stateObj = stateField.get(physicsProvider);
            boolean isResting = stateObj != null && stateObj.toString().equals("Resting");
            Field lastBouncePosField = ProjectileComponent.class.getDeclaredField("lastBouncePosition");
            lastBouncePosField.setAccessible(true);
            Vector3d lastBouncePos = (Vector3d)lastBouncePosField.get(projectile);
            if (!haveHit && !physicsOnGround && lastBouncePos == null && !isResting) {
                return;
            }

            Field positionField = physicsProvider.getClass().getDeclaredField("position");
            positionField.setAccessible(true);
            Vector3d physicsPosition = (Vector3d)positionField.get(physicsProvider);
            Field creatorUuidField = ProjectileComponent.class.getDeclaredField("creatorUuid");
            creatorUuidField.setAccessible(true);
            UUID creatorUuid = (UUID)creatorUuidField.get(projectile);
            if (creatorUuid != null) {
                EntityStore entityStore = (EntityStore)store.getExternalData();
                Ref<EntityStore> ownerRef = entityStore.getRefFromUUID(creatorUuid);
                if (ownerRef != null && (physicsPosition != null || lastBouncePos != null)) {
                    Vector3d targetPos = physicsPosition != null ? physicsPosition : lastBouncePos;
                    Field normalField = physicsProvider.getClass().getDeclaredField("contactNormal");
                    normalField.setAccessible(true);
                    Vector3d normal = (Vector3d)normalField.get(physicsProvider);
                    double offset = 1.2;
                    double targetX = targetPos.x;
                    double targetY = targetPos.y;
                    double targetZ = targetPos.z;


                    if (normal != null) {
                        targetX += normal.x * offset;
                        targetY += normal.y * offset;
                        targetZ += normal.z * offset;
                    } else {
                        targetY += 0.1;
                    }

                    DisplayNameComponent nameComp = (DisplayNameComponent)store.getComponent(ownerRef, DisplayNameComponent.getComponentType());
                    String playerName = null;
                    if (nameComp != null) {
                        playerName = nameComp.getDisplayName().getRawText();
                    }

                    if (playerName != null) {
                        String cmd = "tp " + playerName + " " + targetX + " " + targetY + " " + targetZ;
                        HytaleServer.get().getCommandManager().handleCommand(ConsoleSender.INSTANCE, cmd);
                        creatorUuidField.set(projectile, (Object)null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
