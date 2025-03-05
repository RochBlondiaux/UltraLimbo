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
public class ClientboundSpawnPosition implements ClientboundPacket {

    private long x;
    private long y;
    private long z;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeLong(encodePosition(x, y ,z));
        msg.writeFloat(0);
    }

    private static long encodePosition(long x, long y, long z) {
        return ((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF);
    }
}
