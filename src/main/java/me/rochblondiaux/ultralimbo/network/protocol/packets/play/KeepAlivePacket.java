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
        if (version.moreOrEqual(Version.V1_12_2)) {
            msg.writeLong(id);
        } else if (version.moreOrEqual(Version.V1_8)) {
            msg.writeVarInt((int) id);
        } else {
            msg.writeInt((int) id);
        }
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            this.id = msg.readLong();
        } else if (version.moreOrEqual(Version.V1_8)) {
            this.id = msg.readVarInt();
        } else {
            this.id = msg.readInt();
        }
    }
}
