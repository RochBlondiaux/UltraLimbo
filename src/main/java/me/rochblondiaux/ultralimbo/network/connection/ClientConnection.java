package me.rochblondiaux.ultralimbo.network.connection;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.network.connection.pipeline.PacketDecoder;
import me.rochblondiaux.ultralimbo.network.connection.pipeline.PacketEncoder;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.KeepAlivePacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.State;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@Getter
@Log4j2
public class ClientConnection extends ChannelInboundHandlerAdapter {

    private final Limbo app;
    private final Channel channel;

    private UUID uniqueId;
    private String username;

    private final PacketDecoder decoder;
    private final PacketEncoder encoder;

    private State state;
    private Version clientVersion;
    private SocketAddress address;

    private int velocityLoginMessageId = -1;
    private long lastKeepAlive;
    private long lastKeepAliveSent;
    private long latency;

    public ClientConnection(Channel channel, Limbo app, PacketDecoder decoder, PacketEncoder encoder) {
        this.app = app;
        this.channel = channel;
        this.decoder = decoder;
        this.encoder = encoder;
        this.address = channel.remoteAddress();
        this.lastKeepAlive = System.currentTimeMillis();
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        if (state.equals(State.PLAY) || state.equals(State.CONFIGURATION))
            this.app.connections().remove(this);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (channel.isActive())
            log.error("Exception caught: ", cause);
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
        if (!(msg instanceof ServerboundPacket packet))
            return;

        this.app.networkManager().handleIncoming(this, packet);
    }

    public void sendKeepAlive() {
        this.lastKeepAliveSent = System.currentTimeMillis();
        this.sendPacket(new KeepAlivePacket(this.lastKeepAliveSent));
    }

    public void handleKeepAlive(long id) {
        if (id != this.lastKeepAliveSent)
            return;

        this.latency = System.currentTimeMillis() - this.lastKeepAliveSent;
        this.lastKeepAlive = System.currentTimeMillis();
    }

    public void sendPacket(ClientboundPacket packet) {
        if (isConnected())
            channel.writeAndFlush(packet, channel.voidPromise());
    }

    public void sendPacketAndClose(ClientboundPacket packet) {
        if (isConnected())
            channel.writeAndFlush(packet).addListener(ChannelFutureListener.CLOSE);
    }

    public void writePacket(ClientboundPacket packet) {
        if (isConnected())
            channel.write(packet, channel.voidPromise());
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    public void updateState(State state) {
        this.state = state;
        decoder.updateState(state);
        encoder.updateState(state);
    }

    public void updateEncoderState(State state) {
        encoder.updateState(state);
    }

    public void updateVersion(Version version) {
        clientVersion = version;
        decoder.updateVersion(version);
        encoder.updateVersion(version);
    }

    public void updateProfile(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;
    }

    public void setAddress(String host) {
        this.address = new InetSocketAddress(host, ((InetSocketAddress) this.address).getPort());
    }

}
