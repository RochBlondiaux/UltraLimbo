package me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerboundPlayerLoaded implements ServerboundPacket {

    private String locale;

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.locale = msg.readString();
    }
}
