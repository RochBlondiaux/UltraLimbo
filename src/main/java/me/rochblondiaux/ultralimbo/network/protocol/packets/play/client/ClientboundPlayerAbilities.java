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
public class ClientboundPlayerAbilities implements ClientboundPacket {

    private int flags = 0x02;
    private float flyingSpeed = 0.0F;
    private float fieldOfView = 0.1F;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeByte(flags);
        msg.writeFloat(flyingSpeed);
        msg.writeFloat(fieldOfView);
    }
}
