package me.rochblondiaux.ultralimbo.network.connection.pipeline;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.State;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@Log4j2
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    private State.PacketRegistry mappings;
    private Version version;

    public PacketDecoder() {
        updateVersion(Version.getMin());
        updateState(State.HANDSHAKING);
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (!context.channel().isActive())
            return;

        ByteMessage msg = new ByteMessage(byteBuf);
        int packetId = msg.readVarInt();
        ServerboundPacket packet = mappings.getServerboundPacket(packetId);
        if (packet == null) {
            log.debug("Undefined incoming packet: 0x{}", Integer.toHexString(packetId));
            return;
        }

        log.debug("Received packet {}[0x{}] ({} bytes)", packet.toString(), Integer.toHexString(packetId), msg.readableBytes());
        try {
            packet.decode(msg, version);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Cannot decode packet 0x{}", Integer.toHexString(packetId), e);
            } else {
                log.warn("Cannot decode packet 0x{}: {}", Integer.toHexString(packetId), e.getMessage());
            }
        }

        context.fireChannelRead(packet);
    }

    public void updateVersion(Version version) {
        this.version = version;
    }

    public void updateState(State state) {
        this.mappings = state.serverBound.getRegistry(version);
    }
}
