package com.example.plugin.commands;

import com.example.plugin.Bedwars;
import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.commands.player.PlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Locale;

public class DeployCommand extends CommandBase {

    private final Bedwars plugin;

    public DeployCommand(Bedwars plugin) {
        super("deploy", "Deploys the bedwars game sequence into the world.", false);
        this.plugin = plugin;
        this.setAllowsExtraArguments(true);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        // Read input and its arguments.
        String input = context.getInputString();
        String normalized = input == null ? "" : input.toLowerCase(Locale.ROOT).trim();
        String[] parts = normalized.isEmpty() ? new String[0] : normalized.split("\\s+");
        String sub = parts.length >= 2 ? parts[1] : "";

        if (!sub.isEmpty() && !sub.equals("help")) {

            // ONES (1v1, 8 player FFA)
            if (sub.equals("1vx8")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.noPermission()).color(Color.RED));
                } else {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.deployedMessage(GAMEMODE.ONES)).color(Color.RED));
                    Bedwars.deploy(GAMEMODE.ONES);
                }
            }

            // TWOS (2v2, 16 players divided into pairs.
            else if (sub.equals("2vx8")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.noPermission()).color(Color.RED));
                } else {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.deployedMessage(GAMEMODE.TWOS)).color(Color.RED));
                    Bedwars.deploy(GAMEMODE.ONES);
                }
            }

            // THREES (3v3, 12 players divided into threes)
            else if (sub.equals("3vx4")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.noPermission()).color(Color.RED));
                } else {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.deployedMessage(GAMEMODE.THREES)).color(Color.RED));
                    Bedwars.deploy(GAMEMODE.ONES);
                }
            }

            // FOURS (4vx4, 16 players divided into fours)
            else if (sub.equals("4vx4")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.noPermission()).color(Color.RED));
                } else {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.deployedMessage(GAMEMODE.FOURS)).color(Color.RED));
                    Bedwars.deploy(GAMEMODE.ONES);
                }
            }

            // FOUR AGAINST FOUR (4v4, 8 players divided in half)
            else if (sub.equals("4v4")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.noPermission()).color(Color.RED));
                } else {
                    context.sendMessage(Message.raw(BedwarsCommandMessages.deployedMessage(GAMEMODE.FOURAFOUR)).color(Color.RED));
                    Bedwars.deploy(GAMEMODE.ONES);
                }
            }
        }

        // Send help message.
        else {
            context.sendMessage(Message.raw(BedwarsCommandMessages.deployHelp()).color(Color.CYAN));
        }

    }
}
