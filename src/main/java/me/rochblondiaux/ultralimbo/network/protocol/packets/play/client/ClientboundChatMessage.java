package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.text.Component;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundChatMessage implements ClientboundPacket {

    private UUID sender;
    private LegacyPosition position;
    private Component component;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeComponent(component);

        if (version.moreOrEqual(Version.V1_19_1)) {
            msg.writeBoolean(position.equals(LegacyPosition.ACTION_BAR));
        } else if (version.moreOrEqual(Version.V1_19)) {
            msg.writeVarInt(position.ordinal());
        } else if (version.moreOrEqual(Version.V1_8)) {
            msg.writeByte(position.ordinal());
        }

        if (version.moreOrEqual(Version.V1_16) && version.less(Version.V1_19))
            msg.writeUuid(sender);
    }

    public enum LegacyPosition {
        CHAT,
        SYSTEM_MESSAGE,
        ACTION_BAR
    }
}
