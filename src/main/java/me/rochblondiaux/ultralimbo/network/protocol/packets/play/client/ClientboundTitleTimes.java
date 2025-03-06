package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.title.Title;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundTitleTimes implements ClientboundPacket {

    private Title.Times times;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeInt((int) (times.fadeIn().get(ChronoUnit.SECONDS) / 20));
        msg.writeInt((int) (times.stay().get(ChronoUnit.SECONDS) / 20));
        msg.writeInt((int) (times.fadeIn().get(ChronoUnit.SECONDS) / 20));
    }
}
