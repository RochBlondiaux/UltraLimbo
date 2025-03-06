package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundDestroyEntities implements ClientboundPacket {

    private int[] ids;

    public ClientboundDestroyEntities(int id) {
        this.ids = new int[]{id};
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(this.ids.length);
        for (int id : this.ids) {
            msg.writeVarInt(id);
        }
    }

}
