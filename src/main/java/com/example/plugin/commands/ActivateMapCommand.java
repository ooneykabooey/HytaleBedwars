package com.example.plugin.commands;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.messenger.BedwarsMessenger;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class ActivateMapCommand extends AbstractPlayerCommand {


    public ActivateMapCommand() {
        super("activatemap", "Activates this Bedwars map to be playable, will not work if the world is not a registered map.", false);
        this.setAllowsExtraArguments(false);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        BedwarsMap map = Bedwars.getMapFromMaps(world);
        if (map == null) {
            BedwarsMessenger.activateMapExecutionFail(commandContext);
        } else {
            // TODO: Create activator of a map.
            map.setActivated(true);
        }
    }
}
