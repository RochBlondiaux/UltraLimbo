package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import me.rochblondiaux.ultralimbo.utils.Location;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundTeleportEntity implements ClientboundPacket {

    private int id;
    private Location location;
    private boolean onGround;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(this.id);
        msg.writeDouble(this.location.x());
        msg.writeDouble(this.location.y());
        msg.writeDouble(this.location.z());
        msg.writeFloat(this.location.yaw());
        msg.writeFloat(this.location.pitch());
        msg.writeBoolean(this.onGround);
    }
}
