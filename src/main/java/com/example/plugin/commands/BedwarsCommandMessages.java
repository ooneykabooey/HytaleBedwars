package com.example.plugin.commands;

import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.server.core.command.system.CommandSender;

public class BedwarsCommandMessages {

    public static String noPermission() {
        return "You do not have permission to execute this command!";
    }

    public static String deployedMessage(GAMEMODE mode) {
        String output = "";
        switch (mode) {
            case ONES: output = "1v1 x8" ; break;
            case TWOS: output = "2v2 x8"; break;
            case THREES: output = "3v3 x4"; break;
            case FOURS: output = "4v4 x4"; break;
            case FOURAFOUR: output = "4v4"; break;
        }
        return "You have deployed Bedwars into the world with mode " + output + "!";
    }

    public static String deployHelp() {
        return "/deploy \n\t"+
                " 1vx8 <- eight player FFA"+
                "2vx8 <- teams of two"+
                "3vx4 <- teams of three"+
                "4vx4 <- teams of four"+
                "4v4 <- four against four";
    }



}
