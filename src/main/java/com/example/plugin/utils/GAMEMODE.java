package com.example.plugin.utils;

/// @author ooney
/// This enum consists of all bedwars gamemodes.

public enum GAMEMODE {
        ONES(1),
       TWOS(2),
        THREES(3),
        FOURS(4),
        FOURAFOUR(4);

        private final int numPlayersOnTeam;

        GAMEMODE(int numPlayersOnTeam) {
            this.numPlayersOnTeam = numPlayersOnTeam;
        }

        public int getNumPlayersOnTeam() {
            return numPlayersOnTeam;
        }

}


