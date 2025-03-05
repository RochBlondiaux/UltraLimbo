package me.rochblondiaux.ultralimbo.network.protocol.packets.login;

import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

public class LoginAcknowledgedPacket implements ClientboundPacket, ServerboundPacket {

    @Override
    public void encode(ByteMessage msg, Version version) {

    }

    @Override
    public void decode(ByteMessage msg, Version version) {

    }
}
