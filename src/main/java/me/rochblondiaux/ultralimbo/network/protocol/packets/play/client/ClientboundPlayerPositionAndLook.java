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
public class ClientboundPlayerPositionAndLook implements ClientboundPacket {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int teleportId;

    @Override
    public void encode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_21_2)) {
            encodeModern(msg);
            return;
        }

        encodeLegacy(msg, version);
    }


    private void encodeLegacy(ByteMessage msg, Version version) {
        msg.writeDouble(x);
        msg.writeDouble(y + (version.less(Version.V1_8) ? 1.62F : 0));
        msg.writeDouble(z);
        msg.writeFloat(yaw);
        msg.writeFloat(pitch);

        if (version.moreOrEqual(Version.V1_8)) {
            msg.writeByte(0x08);
        } else {
            msg.writeBoolean(true);
        }

        if (version.moreOrEqual(Version.V1_9)) {
            msg.writeVarInt(teleportId);
        }

        if (version.fromTo(Version.V1_17, Version.V1_19_3)) {
            msg.writeBoolean(false); // Dismount vehicle
        }
    }

    private void encodeModern(ByteMessage msg) {
        msg.writeVarInt(teleportId);

        msg.writeDouble(x);
        msg.writeDouble(y);
        msg.writeDouble(z);

        msg.writeDouble(0);
        msg.writeDouble(0);
        msg.writeDouble(0);

        msg.writeFloat(yaw);
        msg.writeFloat(pitch);

        msg.writeInt(0x08);
    }
}
