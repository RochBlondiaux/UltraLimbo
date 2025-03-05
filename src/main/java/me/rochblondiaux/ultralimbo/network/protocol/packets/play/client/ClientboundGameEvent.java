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

    private byte type;
    private float value;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeByte(type);
        msg.writeFloat(value);
    }

}
