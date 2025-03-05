package me.rochblondiaux.ultralimbo.network.protocol.packets.login.client;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundLoginPluginRequest implements ClientboundPacket {

    private int messageId;
    private String channel;
    private ByteBuf data;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(this.messageId);
        msg.writeString(this.channel);
        msg.writeBytes(this.data);
    }
}
