package me.rochblondiaux.ultralimbo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.configuration.implementation.ServerConfiguration;
import me.rochblondiaux.ultralimbo.network.connection.ClientChannelInitializer;

@Log4j2
@RequiredArgsConstructor
public class LimboServer {

    private final Limbo app;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() {
        Class<? extends ServerChannel> channelClass;

        ServerConfiguration.Netty netty = app.configuration().advanced().netty();
        if (netty.useEpoll() && Epoll.isAvailable()) {
            bossGroup = new EpollEventLoopGroup(netty.bossGroupSize());
            workerGroup = new EpollEventLoopGroup(netty.workerGroupSize());
            channelClass = EpollServerSocketChannel.class;
            log.debug("Using Epoll transport type");
        } else {
            bossGroup = new NioEventLoopGroup(netty.bossGroupSize());
            workerGroup = new NioEventLoopGroup(netty.workerGroupSize());
            channelClass = NioServerSocketChannel.class;
            log.debug("Using Java NIO transport type");
        }


        new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(channelClass)
                .childHandler(new ClientChannelInitializer(this.app))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .localAddress(app.configuration().address())
                .bind();

        log.info("Server started on {}", app.configuration().address());
    }

    public void stop() {
        if (bossGroup != null)
            bossGroup.shutdownGracefully();
        if (workerGroup != null)
            workerGroup.shutdownGracefully();
    }
}
