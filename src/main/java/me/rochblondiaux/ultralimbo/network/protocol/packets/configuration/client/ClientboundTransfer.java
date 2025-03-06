package me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundTransfer implements ClientboundPacket {

    private String host;
    private int port;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(this.host, 32767);
        msg.writeShort(this.port);
    }
}
