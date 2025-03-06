package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundRemovePlayerInfo implements ClientboundPacket {

    private List<UUID> uniqueIds;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeList(uniqueIds, ByteMessage::writeUuid);
    }

}
