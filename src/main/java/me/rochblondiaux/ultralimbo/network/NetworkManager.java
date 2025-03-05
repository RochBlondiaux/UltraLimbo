package me.rochblondiaux.ultralimbo.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.configuration.implementation.ServerConfiguration;
import me.rochblondiaux.ultralimbo.network.connection.ClientConnection;
import me.rochblondiaux.ultralimbo.network.protocol.PacketSnapshot;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.ServerboundHandshake;
import me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.FinishConfigurationPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.client.ClientboundRegistryData;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.LoginAcknowledgedPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.client.ClientboundLoginDisconnect;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.client.ClientboundLoginSuccess;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.server.ServerboundLoginStart;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.KeepAlivePacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.StatusPingPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.client.ClientboundStatusResponse;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.server.ServerboundStatusRequest;
import me.rochblondiaux.ultralimbo.network.protocol.registry.State;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import me.rochblondiaux.ultralimbo.world.Dimension;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;

@Log4j2
@RequiredArgsConstructor
public class NetworkManager {

    private final Limbo app;

    public void handleIncoming(ClientConnection connection, ServerboundPacket packet) {
        if (packet instanceof ServerboundHandshake handshakePacket) {
            connection.updateVersion(handshakePacket.version());
            connection.updateState(handshakePacket.nextState());

            log.debug("Pinged from {} [{}]", connection.address(), handshakePacket.version().toString());
        } else if (packet instanceof StatusPingPacket statusPingPacket) {
            connection.sendPacketAndClose(statusPingPacket);
        } else if (packet instanceof ServerboundStatusRequest statusRequestPacket) {
            ServerConfiguration configuration = app.configuration();
            connection.sendPacket(new ClientboundStatusResponse(
                    Version.getMax().protocolNumber(),
                    configuration.motd().version(),
                    configuration.motd().description(),
                    this.app.connections().size(),
                    configuration.maxPlayers()
            ));
        } else if (packet instanceof ServerboundLoginStart loginStartPacket)
            handleLoginStart(connection, loginStartPacket);
        else if (packet instanceof LoginAcknowledgedPacket)
            handleLoginAcknowledge(connection);
        else if (packet instanceof FinishConfigurationPacket)
            app.playerManager().register(connection);
        else if (packet instanceof KeepAlivePacket keepAlivePacket)
            connection.handleKeepAlive(keepAlivePacket.id());
    }

    private void handleLoginStart(ClientConnection connection, ServerboundLoginStart packet) {
        connection.updateProfile(UUID.randomUUID(), packet.username());

        // Server full check
        int maxPlayers = this.app.configuration().maxPlayers();
        if (maxPlayers > 0 && this.app.playerManager().count() > maxPlayers) {
            connection.sendPacketAndClose(new ClientboundLoginDisconnect(this.app.configuration().component("server-full")));
            return;
        }

        // Version check
        if (!connection.clientVersion().isSupported()) {
            connection.sendPacketAndClose(new ClientboundLoginDisconnect(this.app.configuration().component("unsupported-version")));
            return;
        }

        // TODO: Implement info forwarding (with velocity / bungee guard)

        this.fireLoginSuccess(connection);
    }

    private void handleLoginAcknowledge(ClientConnection connection) {
        connection.updateState(State.CONFIGURATION);

        // TODO: Send brand name

        if (connection.clientVersion().moreOrEqual(Version.V1_20_5)) {
            Dimension dimension1_21_4 = this.app.dimensionRegistry().dimension_1_21_4();
            List<PacketSnapshot> packetRegistries = new ArrayList<>();
            CompoundBinaryTag dimensionTag = dimension1_21_4.data();
            for (String registryType : dimensionTag.keySet()) {
                CompoundBinaryTag compoundRegistryType = dimensionTag.getCompound(registryType);
                ListBinaryTag values = compoundRegistryType.getList("value");

                ClientboundRegistryData registryData = new ClientboundRegistryData(this.app.dimensionRegistry(), (message, version) -> {
                    message.writeString(registryType);

                    message.writeVarInt(values.size());
                    for (BinaryTag entry : values) {
                        CompoundBinaryTag entryTag = (CompoundBinaryTag) entry;

                        String name = entryTag.getString("name");
                        CompoundBinaryTag element = entryTag.getCompound("element");

                        message.writeString(name);
                        message.writeBoolean(true);
                        message.writeNamelessCompoundTag(element);
                    }
                });
                packetRegistries.add(PacketSnapshot.of(registryData));
            }

            for (PacketSnapshot packetRegistry : packetRegistries) {
                connection.sendPacket(packetRegistry);
            }
        } else {
            connection.sendPacket(new ClientboundRegistryData(this.app.dimensionRegistry(), null));
        }

        connection.sendPacket(new FinishConfigurationPacket());
    }

    private void fireLoginSuccess(ClientConnection connection) {
        connection.sendPacket(new ClientboundLoginSuccess(
                connection.uniqueId(),
                connection.username()
        ));

        // Preparing for configuration mode
        if (connection.clientVersion().moreOrEqual(Version.V1_20_2)) {
            connection.updateEncoderState(State.CONFIGURATION);
            return;
        }

        this.app.playerManager().register(connection);
    }
}
