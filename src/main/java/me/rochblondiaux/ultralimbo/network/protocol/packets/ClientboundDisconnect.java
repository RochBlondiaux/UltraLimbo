package me.rochblondiaux.ultralimbo.network.protocol.packets;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.text.Component;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundDisconnect implements ClientboundPacket {

    private Component reason;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeComponent(reason);
    }
}
