package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import java.util.EnumSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import me.rochblondiaux.ultralimbo.player.PlayerInfo;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundUpdatePlayerInfo implements ClientboundPacket {

    private EnumSet<Action> actions;
    private List<PlayerInfo> playerInfos;

    public ClientboundUpdatePlayerInfo(Action action, PlayerInfo info) {
        this(EnumSet.of(action), List.of(info));
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeEnumSet(actions, Action.class);

        msg.writeList(playerInfos, (message, playerInfo) -> {
            for (Action action : actions) {
                msg.writeUuid(playerInfo.profile().uuid());
                switch (action) {
                    case ADD_PLAYER -> {
                        msg.writeString(playerInfo.profile().name());
                        msg.writeList(playerInfo.profile().textureProperties(), (msg1, textureProperty) -> {
                            msg1.writeString(textureProperty.name());
                            msg1.writeString(textureProperty.value());
                            msg1.writeString(textureProperty.signature());
                        });
                    }
                    case INITIALIZE_CHAT -> {
                    }
                    case UPDATE_GAME_MODE -> msg.writeVarInt(playerInfo.gameMode().ordinal());
                    case UPDATE_LATENCY -> msg.writeVarInt(playerInfo.latency());
                    case UPDATE_DISPLAY_NAME ->
                            msg.writeOptional(playerInfo.displayName(), ByteMessage::writeComponent);
                    case UPDATE_LISTED -> msg.writeBoolean(playerInfo.listed());
                }
            }
        });
    }

    public enum Action {
        ADD_PLAYER,
        INITIALIZE_CHAT,
        UPDATE_GAME_MODE,
        UPDATE_LISTED,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME;
    }
}
