package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.text.Component;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundPlayerListHeader implements ClientboundPacket {

    private Component header;
    private Component footer;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeComponent(header);
        msg.writeComponent(footer);
    }
}
