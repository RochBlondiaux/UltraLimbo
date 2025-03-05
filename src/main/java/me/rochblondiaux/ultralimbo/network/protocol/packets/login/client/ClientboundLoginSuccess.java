package me.rochblondiaux.ultralimbo.network.protocol.packets.login.client;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundLoginSuccess implements ClientboundPacket {

    private UUID uniqueId;
    private String username;

    @Override
    public void encode(ByteMessage msg, Version version) {
        // Unique id
        if (version.moreOrEqual(Version.V1_16))
            msg.writeUuid(this.uniqueId);
        else if (version.moreOrEqual(Version.V1_7_6))
            msg.writeString(this.uniqueId.toString());
        else
            msg.writeString(this.uniqueId.toString().replace("-", ""));

        // Username
        msg.writeString(this.username);

        // Properties
        if (version.moreOrEqual(Version.V1_19))
            msg.writeVarInt(0);
        if (version.fromTo(Version.V1_20_5, Version.V1_21))
            msg.writeBoolean(true);
    }
}
