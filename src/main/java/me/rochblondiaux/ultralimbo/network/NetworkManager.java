package me.rochblondiaux.ultralimbo.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.configuration.implementation.ServerConfiguration;
import me.rochblondiaux.ultralimbo.network.connection.ClientConnection;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.ServerboundHandshake;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.StatusPingPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.client.ClientboundStatusResponse;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.server.ServerboundStatusRequest;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

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
        }
    }

}
