package com.example.plugin.commands;

import com.example.plugin.file.BedwarsShopIO;
import com.example.plugin.ui.HyUIShopPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class CreateShopCommand extends AbstractPlayerCommand {

    BedwarsShopIO bedwarsShopIO;

    public CreateShopCommand() {
        super("defaultshop", "Running this command will create a default shop .json in the bedwars_shop folder at your server directory", false);
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        bedwarsShopIO.createDefault();
    }
}
