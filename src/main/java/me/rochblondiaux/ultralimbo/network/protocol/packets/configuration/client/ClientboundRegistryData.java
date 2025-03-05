package me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.MetadataWriter;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import me.rochblondiaux.ultralimbo.world.DimensionRegistry;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundRegistryData implements ClientboundPacket {

    private DimensionRegistry dimensionRegistry;
    private MetadataWriter metadataWriter;

    @Override
    public void encode(ByteMessage msg, Version version) {
        if (this.metadataWriter != null && version.moreOrEqual(Version.V1_20_5)) {
            metadataWriter.writeData(msg, version);
            return;
        }

        msg.writeNamelessCompoundTag(dimensionRegistry.codec_1_20());
    }
}
