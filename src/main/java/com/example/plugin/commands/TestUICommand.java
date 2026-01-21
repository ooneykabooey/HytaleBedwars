package com.example.plugin.commands;

import com.example.plugin.ui.TestUIPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

///  @author  yasha

public class TestUICommand extends AbstractPlayerCommand {

    public TestUICommand() {
        super("uitest", "Opens test UI", false);
    }

    @Override
    protected void execute(CommandContext crx, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        TestUIPage page = new TestUIPage(playerRef);

        player.getPageManager().openCustomPage(ref, store, page);
    }
}
