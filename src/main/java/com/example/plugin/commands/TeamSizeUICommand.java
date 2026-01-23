package com.example.plugin.commands;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.ui.TeamSizeUIPage;
import com.example.plugin.ui.WelcomeUIPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

///  @author  yasha
@Deprecated // don't use, it will break the registry by skipping certain things.
public class TeamSizeUICommand extends AbstractPlayerCommand {

    public TeamSizeUICommand() {
        super("teamsize", "Opens test UI", false);
    }

    @Override
    protected void execute(CommandContext crx, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        TeamSizeUIPage page = new TeamSizeUIPage(playerRef, new BedwarsMap(player.getWorld()));

        player.getPageManager().openCustomPage(ref, store, page);
    }
}
