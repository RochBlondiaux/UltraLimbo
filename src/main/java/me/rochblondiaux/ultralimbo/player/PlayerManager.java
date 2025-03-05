package me.rochblondiaux.ultralimbo.player;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Unmodifiable;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.network.connection.ClientConnection;
import me.rochblondiaux.ultralimbo.network.connection.PacketSnapshots;
import me.rochblondiaux.ultralimbo.network.protocol.PacketSnapshot;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundPlayerPositionAndLook;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundSpawnPosition;
import me.rochblondiaux.ultralimbo.network.protocol.registry.State;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@RequiredArgsConstructor
@Log4j2
public class PlayerManager {

    private final Limbo app;
    private final Map<UUID, Player> players = new ConcurrentHashMap<>();

    public void register(ClientConnection connection) {
        Player player = new Player(connection);
        this.players.put(player.uniqueId(), player);

        this.spawn(player, connection);

        log.info("Player {} ({}) has joined the server", player.name(), player.uniqueId());
    }

    private void spawn(Player player, ClientConnection connection) {
        connection.updateState(State.PLAY);

        Runnable sendPlayPackets = () -> {
            connection.sendPacket(PacketSnapshots.PACKET_JOIN_GAME);
            connection.sendPacket(PacketSnapshots.PACKET_PLAYER_ABILITIES);

            connection.sendPacket(new ClientboundPlayerPositionAndLook(
                    0,
                    400,
                    0,
                    0,
                    0,
                    0
            ));

            if (connection.clientVersion().moreOrEqual(Version.V1_19_3))
                connection.sendPacket(new ClientboundSpawnPosition(
                        0,
                        400,
                        0
                ));

            connection.sendPacket(PacketSnapshots.PACKET_DECLARE_COMMANDS);

            // TODO: implement real map support
            if (connection.clientVersion().moreOrEqual(Version.V1_20_3)) {
                connection.sendPacket(PacketSnapshots.PACKET_START_WAITING_CHUNKS);
                for (PacketSnapshot packetsEmptyChunk : PacketSnapshots.PACKETS_EMPTY_CHUNKS) {
                    connection.sendPacket(packetsEmptyChunk);
                }
            }
        };

        if (connection.clientVersion().lessOrEqual(Version.V1_7_6)) {
            connection.channel().eventLoop().schedule(sendPlayPackets, 100, TimeUnit.MILLISECONDS);
        } else {
            sendPlayPackets.run();
        }
    }

    public void unregister(UUID uniqueId) {
        Player player = this.players.remove(uniqueId);
        if (player != null)
            log.info("Player {} ({}) has left the server", player.name(), player.uniqueId());
    }

    public Optional<Player> findByUniqueId(UUID uniqueId) {
        return Optional.ofNullable(this.players.get(uniqueId));
    }

    public Optional<Player> findByName(String name) {
        return this.players.values().stream()
                .filter(player -> player.name().equals(name))
                .findFirst();
    }

    public int count() {
        return this.players.size();
    }

    @Unmodifiable
    public Collection<Player> players() {
        return this.players.values();
    }

}
