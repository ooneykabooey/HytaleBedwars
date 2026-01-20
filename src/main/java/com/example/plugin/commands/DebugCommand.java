package com.example.plugin.commands;

import com.example.plugin.Bedwars;
import com.example.plugin.messenger.BedwarsMessenger;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;

public class DebugCommand extends CommandBase {

    private final Bedwars plugin;

    public DebugCommand(Bedwars plugin) {
        super("debugbedwars", "Activates the debug mode for the bedwars plugin.", false);
        this.plugin = plugin;
        this.setAllowsExtraArguments(false);
    }


    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        BedwarsMessenger.toggleDebug(commandContext);
    }
}
