package me.rochblondiaux.ultralimbo.network.protocol.packets.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KeepAlivePacket implements ClientboundPacket, ServerboundPacket {

    private long id;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeLong(this.id);
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.id = msg.readInt();
    }
}
