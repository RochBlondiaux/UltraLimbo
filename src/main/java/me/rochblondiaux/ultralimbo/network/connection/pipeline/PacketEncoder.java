/*
 * Copyright (C) 2020 Nan1t
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.rochblondiaux.ultralimbo.network.connection.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.PacketSnapshot;
import me.rochblondiaux.ultralimbo.network.protocol.registry.State;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@Log4j2
public class PacketEncoder extends MessageToByteEncoder<ClientboundPacket> {

    private State.PacketRegistry registry;
    private Version version;

    public PacketEncoder() {
        updateVersion(Version.getMin());
        updateState(State.HANDSHAKING);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ClientboundPacket packet, ByteBuf out) throws Exception {
        if (registry == null) return;

        ByteMessage msg = new ByteMessage(out);
        int packetId;

        if (packet instanceof PacketSnapshot snapshot) {
            packetId = registry.getPacketId(snapshot.getWrappedPacket().getClass());
        } else {
            packetId = registry.getPacketId(packet.getClass());
        }

        if (packetId == -1) {
            log.warn("Undefined packet class: {}[0x{}] ({} bytes)", packet.getClass().getName(), Integer.toHexString(packetId), msg.readableBytes());
            return;
        }

        msg.writeVarInt(packetId);

        try {
            packet.encode(msg, version);

            log.debug("Sending {}[0x{}] packet ({} bytes)", packet.toString(), Integer.toHexString(packetId), msg.readableBytes());
        } catch (Exception e) {
            log.error("Cannot encode packet 0x{}: {}", Integer.toHexString(packetId), e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateVersion(Version version) {
        this.version = version;
    }

    public void updateState(State state) {
        this.registry = state.clientBound.getRegistry(version);
    }

}
