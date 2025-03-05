package me.rochblondiaux.ultralimbo.network.protocol.packets.login.server;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerboundLoginStart implements ServerboundPacket {

    private String username;

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.username = msg.readString();
    }

}
