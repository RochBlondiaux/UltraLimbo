package me.rochblondiaux.ultralimbo.network.connection;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.configuration.implementation.ServerConfiguration;
import me.rochblondiaux.ultralimbo.network.connection.pipeline.*;

@RequiredArgsConstructor
public class ClientChannelInitializer extends ChannelInitializer<Channel> {

    private final Limbo app;

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        PacketDecoder decoder = new PacketDecoder();
        PacketEncoder encoder = new PacketEncoder();
        ClientConnection connection = new ClientConnection(channel, app, decoder, encoder);
        this.app.connections().add(connection);

        ServerConfiguration.Advanced advanced = app.configuration().advanced();

        pipeline.addLast("timeout", new ReadTimeoutHandler(advanced.readTimeout(), TimeUnit.MILLISECONDS));
        pipeline.addLast("frame_decoder", new VarIntFrameDecoder());
        pipeline.addLast("frame_encoder", new VarIntLengthEncoder());

        if (advanced.traffic().enabled())
            pipeline.addLast("traffic_limit", new ChannelTrafficHandler(
                    advanced.traffic().maxPacketSize(),
                    advanced.traffic().interval(),
                    advanced.traffic().maxPacketRate()
            ));

        pipeline.addLast("decoder", decoder);
        pipeline.addLast("encoder", encoder);
        pipeline.addLast("handler", connection);
    }
}
