package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTag;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClientboundEmptyChunk implements ClientboundPacket {

    private int x;
    private int z;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeInt(x);
        msg.writeInt(z);

        LongArrayBinaryTag longArrayTag = LongArrayBinaryTag.longArrayBinaryTag(new long[37]);
        CompoundBinaryTag tag = CompoundBinaryTag.builder()
                .put("MOTION_BLOCKING", longArrayTag).build();
        CompoundBinaryTag rootTag = CompoundBinaryTag.builder()
                .put("root", tag).build();
        msg.writeNamelessCompoundTag(rootTag);

        byte[] sectionData = new byte[]{0, 0, 0, 0, 0, 0, 1, 0};
        msg.writeVarInt(sectionData.length * 16);
        for (int i = 0; i < 16; i++) {
            msg.writeBytes(sectionData);
        }

        msg.writeVarInt(0);

        byte[] lightData = new byte[]{1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 3, -1, -1, 0, 0};
        msg.ensureWritable(lightData.length);
        msg.writeBytes(lightData, 1, lightData.length - 1);
    }
}
