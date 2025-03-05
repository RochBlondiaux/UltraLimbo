package me.rochblondiaux.ultralimbo.network.protocol.packets.login.server;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ServerboundLoginPluginResponse implements ServerboundPacket {

    private int messageId;
    private boolean successful;
    private ByteMessage data;

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.messageId = msg.readVarInt();
        this.successful = msg.readBoolean();

        if (msg.readableBytes() > 0) {
            int i = msg.readableBytes();
            data = new ByteMessage(msg.readBytes(i));
        }
    }
}
