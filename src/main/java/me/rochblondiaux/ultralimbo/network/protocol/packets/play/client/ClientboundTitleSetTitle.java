package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.text.Component;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundTitleSetTitle implements ClientboundPacket {

    private Component component;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeComponent(component);
    }
}
