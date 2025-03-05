package me.rochblondiaux.ultralimbo.player;

public enum GameMode {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    private static final GameMode[] VALUES = values();

    public int getId() {
        return ordinal();
    }

    public static GameMode getById(int id) {
        // Minecraft defaults to a survival gamemode if invalid
        if (id < 0 || id >= VALUES.length) {
            return GameMode.SURVIVAL;
        }
        return VALUES[id];
    }

    public static GameMode defaultGameMode() {
        return SURVIVAL;
    }
}