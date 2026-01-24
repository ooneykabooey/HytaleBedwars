package com.example.plugin.utils;

/// @author yasha
/// This enum consists of all bedwars teams and their colors.

public enum TeamColor {
        RED("Red"),
        BLUE("Blue"),
        GREEN("Green"),
        YELLOW("Yellow"),
        CYAN("Cyan"),
        PINK("Pink"),
        PURPLE("Purple"),
        ORANGE("Orange"),
        WHITE("White"),
        BLACK("Black");

        private final String displayName;

        TeamColor(String displayName) {
                this.displayName = displayName;
        }

        public String getDisplayName() {
                return displayName;
        }
}

