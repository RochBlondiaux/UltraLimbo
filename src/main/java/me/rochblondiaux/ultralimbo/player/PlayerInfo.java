package me.rochblondiaux.ultralimbo.player;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

@Getter
@Setter
public class PlayerInfo {

    private UserProfile profile;
    private boolean listed = true;
    private int latency;
    private GameMode gameMode;
    @Nullable
    private Component displayName;
    /**
     * Added with 1.21.2
     */
    private int listOrder;
    /**
     * Added with 1.21.4
     */
    private boolean showHat;

    public PlayerInfo(UUID profileId) {
        this(new UserProfile(profileId, ""));
    }

    public PlayerInfo(UserProfile profile) {
        this.profile = profile;
    }

    public PlayerInfo(
            UserProfile profile, boolean listed,
            int latency, GameMode gameMode,
            @Nullable Component displayName
    ) {
        this(profile, listed, latency, gameMode, displayName, 0);
    }

    public PlayerInfo(
            UserProfile profile, boolean listed,
            int latency, GameMode gameMode,
            @Nullable Component displayName,
            int listOrder
    ) {
        this(profile, listed, latency, gameMode, displayName, listOrder, false);
    }

    public PlayerInfo(
            UserProfile profile, boolean listed,
            int latency, GameMode gameMode,
            @Nullable Component displayName,
            int listOrder, boolean showHat
    ) {
        this.profile = profile;
        this.listed = listed;
        this.latency = latency;
        this.gameMode = gameMode;
        this.displayName = displayName;
        this.listOrder = listOrder;
        this.showHat = showHat;
    }

}
