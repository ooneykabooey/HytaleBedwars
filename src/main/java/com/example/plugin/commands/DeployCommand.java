package com.example.plugin.commands;

import com.example.plugin.Bedwars;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

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

        // TODO: For-loop this with a helper method in a later implementation because every new gamemode will add another else-if
        if (!sub.isEmpty() && !sub.equals("help")) {

            // ONES (1v1, 8 player FFA)
            if (sub.equals("1vx8")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    BedwarsMessenger.noPermission(context);
                } else {
                    BedwarsMessenger.deployedMessage(context, GAMEMODE.ONES);
                    Bedwars.deploy(GAMEMODE.ONES);
                }
            }

            // TWOS (2v2, 16 players divided into pairs.
            else if (sub.equals("2vx8")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    BedwarsMessenger.noPermission(context);
                } else {
                    BedwarsMessenger.deployedMessage(context, GAMEMODE.TWOS);
                    Bedwars.deploy(GAMEMODE.TWOS);
                }
            }

            // THREES (3v3, 12 players divided into threes)
            else if (sub.equals("3vx4")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    BedwarsMessenger.noPermission(context);
                } else {
                    BedwarsMessenger.deployedMessage(context, GAMEMODE.THREES);
                    Bedwars.deploy(GAMEMODE.THREES);
                }
            }

            // FOURS (4vx4, 16 players divided into fours)
            else if (sub.equals("4vx4")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    BedwarsMessenger.noPermission(context);
                } else {
                    BedwarsMessenger.deployedMessage(context, GAMEMODE.FOURS);
                    Bedwars.deploy(GAMEMODE.FOURS);
                }
            }

            // FOUR AGAINST FOUR (4v4, 8 players divided in half)
            else if (sub.equals("4v4")) {
                if (!context.sender().hasPermission("bedwars.op")) {
                    BedwarsMessenger.noPermission(context);
                } else {
                    BedwarsMessenger.deployedMessage(context, GAMEMODE.FOURAFOUR);
                    Bedwars.deploy(GAMEMODE.FOURAFOUR);
                }
            }
        }

        // Send help message.
        else {
            BedwarsMessenger.deployHelp(context);
        }

    }
}
