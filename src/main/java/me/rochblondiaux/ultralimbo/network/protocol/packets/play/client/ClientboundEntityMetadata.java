package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.entity.metadata.EntityData;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundEntityMetadata implements ClientboundPacket {

    private int id;
    private List<EntityData> entityData;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(this.id);
        msg.writeMetadata(this.entityData);
    }
}
