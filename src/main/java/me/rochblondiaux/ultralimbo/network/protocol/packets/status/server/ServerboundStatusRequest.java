package me.rochblondiaux.ultralimbo.network.protocol.packets.status.server;

import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

public class ServerboundStatusRequest implements ServerboundPacket {

    @Override
    public void decode(ByteMessage msg, Version version) {

    }
}
