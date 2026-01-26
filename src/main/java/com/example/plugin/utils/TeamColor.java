package com.example.plugin.utils;

import java.awt.*;

/// @author yasha
/// This enum consists of all bedwars teams and their colors.

public enum TeamColor {
        RED("Red", Color.RED),
        BLUE("Blue", Color.BLUE),
        GREEN("Green", Color.GREEN),
        YELLOW("Yellow", Color.YELLOW),
        CYAN("Cyan", Color.CYAN),
        PINK("Pink", Color.PINK),
        PURPLE("Purple", new Color(172, 0, 254)),
        ORANGE("Orange", Color.ORANGE),
        WHITE("White", Color.WHITE),
        BLACK("Black", new Color(29,29,29));

        private final String displayName;
        private final Color color;

        TeamColor(String displayName, Color color) {
                this.displayName = displayName;
                this.color = color;
        }

        public String getDisplayName() {
                return displayName;
        }

        public Color getColor() {
            return color;
        }
}

