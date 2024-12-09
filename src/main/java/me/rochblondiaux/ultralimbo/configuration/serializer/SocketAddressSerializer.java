package me.rochblondiaux.ultralimbo.configuration.serializer;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class SocketAddressSerializer implements TypeSerializer<SocketAddress> {

    public static final SocketAddressSerializer INSTANCE = new SocketAddressSerializer();

    @Override
    public SocketAddress deserialize(Type type, ConfigurationNode node) {
        String ip = node.node("host").getString();
        int port = node.node("port").getInt();
        if (ip == null || ip.isEmpty())
            return new InetSocketAddress(port);
        return new InetSocketAddress(ip, port);
    }

    @Override
    public void serialize(Type type, @Nullable SocketAddress obj, ConfigurationNode node) {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}
