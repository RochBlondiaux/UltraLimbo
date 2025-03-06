package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClientboundGameEvent implements ClientboundPacket {

    private Type type;
    private float value;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeByte(type.ordinal());
        msg.writeFloat(value);
    }

    public enum Type {
        NO_RESPAWN_BLOCK_AVAILABLE,
        BEGIN_RAINING,
        END_RAINING,
        CHANGE_GAME_MODE,
        WIN_GAME,
        DEMO_EVENT,
        ARROW_HIT_PLAYER,
        RAIN_LEVEL_CHANGE,
        THUNDER_LEVEL_CHANGE,
        PUFFER_FISH_STING,
        GUARDIAN_ELDER_EFFECT,
        ENABLE_RESPAWN_SCREEN,
        LIMITED_CRAFTING,
        START_WAITING_FOR_LEVEL_CHUNKS
    }
}
