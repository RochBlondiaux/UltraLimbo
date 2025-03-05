package me.rochblondiaux.ultralimbo.network.protocol.packets.play.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@Getter
public class ServerboundChatCommand implements ServerboundPacket {

    private String command;

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.command = msg.readString();
    }
}
