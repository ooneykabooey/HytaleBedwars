package com.example.plugin.utils;

/// @author yasha
/// This enum consists of all bedwars teams and their colors.

public enum TeamColor {
        RED("#79342b", "Red"),
        BLUE("#26405d", "Blue"),
        GREEN("#317447", "Green"),
        YELLOW("#d5a631", "Yellow"),
        CYAN("#377b7c", "Cyan"),
        PINK("#974f6e", "Pink"),
        PURPLE("#471937", "Purple"),
        ORANGE("#91621e", "Orange"),
        WHITE("#e1e1e1", "White"),
        BLACK("#141414", "Black");

        public final String hex;
        private final String displayName;

        TeamColor(String hex, String displayName) {
                this.hex = hex;
                this.displayName = displayName;
        }

        public String getDisplayName() {
                return displayName;
        }
}

