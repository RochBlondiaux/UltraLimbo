package me.rochblondiaux.ultralimbo.network.connection;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.network.protocol.PacketSnapshot;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.*;
import me.rochblondiaux.ultralimbo.player.GameMode;

@UtilityClass
public class PacketSnapshots {

    public static PacketSnapshot PACKET_JOIN_GAME;
    public static PacketSnapshot PACKET_PLUGIN_MESSAGE;
    public static PacketSnapshot PACKET_PLAYER_ABILITIES;
    public static PacketSnapshot PACKET_PLAYER_INFO;
    public static PacketSnapshot PACKET_DECLARE_COMMANDS;
    public static PacketSnapshot PACKET_START_WAITING_CHUNKS;
    public static List<PacketSnapshot> PACKETS_EMPTY_CHUNKS = new ArrayList<>();

    public static void initPackets(Limbo app) {
        // Join game
        String worldName = "minecraft:%s".formatted(app.configuration().dimension());
        ClientboundJoinGame joinGamePacket = new ClientboundJoinGame(
                0,
                false,
                GameMode.SURVIVAL,
                null,
                new String[]{worldName},
                app.dimensionRegistry(),
                worldName,
                0,
                app.configuration().maxPlayers(),
                0,
                false,
                false,
                false,
                false,
                false,
                false
        );
        PACKET_JOIN_GAME = PacketSnapshot.of(joinGamePacket);

        // Player abilities
        ClientboundPlayerAbilities playerAbilitiesPacket = new ClientboundPlayerAbilities();
        PACKET_PLAYER_ABILITIES = PacketSnapshot.of(playerAbilitiesPacket);

        // Declare commands
        ClientboundDeclareCommands declareCommandsPacket = new ClientboundDeclareCommands(new ArrayList<>(app.commands().commands().keySet()));
        PACKET_DECLARE_COMMANDS = PacketSnapshot.of(declareCommandsPacket);

        // Wait for chunks
        ClientboundGameEvent gameEvent = new ClientboundGameEvent((byte) 13, 0);
        PACKET_START_WAITING_CHUNKS = PacketSnapshot.of(gameEvent);

        // Empty chunks
        int chunkXOffset = (int) 0 >> 4; // Default x position is 0
        int chunkZOffset = (int) 0 >> 4; // Default z position is 0
        int chunkEdgeSize = 1; // TODO Make configurable?
        List<PacketSnapshot> emptyChunks = new ArrayList<>();
        // Make multiple chunks for edges
        for (int chunkX = chunkXOffset - chunkEdgeSize; chunkX <= chunkXOffset + chunkEdgeSize; ++chunkX) {
            for (int chunkZ = chunkZOffset - chunkEdgeSize; chunkZ <= chunkZOffset + chunkEdgeSize; ++chunkZ) {
                emptyChunks.add(PacketSnapshot.of(new ClientboundEmptyChunk(
                        chunkX,
                        chunkZ
                )));
            }
        }
        PACKETS_EMPTY_CHUNKS = emptyChunks;
    }

}
