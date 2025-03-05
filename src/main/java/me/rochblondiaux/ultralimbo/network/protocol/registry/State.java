package me.rochblondiaux.ultralimbo.network.protocol.registry;

import java.util.*;
import java.util.function.Supplier;

import lombok.Getter;
import me.rochblondiaux.ultralimbo.network.protocol.Packet;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.ServerboundHandshake;
import me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.FinishConfigurationPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.client.ClientboundRegistryData;
import me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.server.ServerboundPlayerLoaded;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.LoginAcknowledgedPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.client.ClientboundLoginDisconnect;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.client.ClientboundLoginPluginRequest;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.client.ClientboundLoginSuccess;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.server.ServerboundLoginPluginResponse;
import me.rochblondiaux.ultralimbo.network.protocol.packets.login.server.ServerboundLoginStart;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.KeepAlivePacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.*;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.StatusPingPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.client.ClientboundStatusResponse;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.server.ServerboundStatusRequest;
import static me.rochblondiaux.ultralimbo.network.protocol.registry.Version.*;

public enum State {
    HANDSHAKING(0) {
        {
            serverBound.register(ServerboundHandshake::new,
                    map(0x00, getMin(), getMax())
            );
        }
    },
    STATUS(1) {
        {
            serverBound.register(ServerboundStatusRequest::new,
                    map(0x00, getMin(), getMax())
            );
            serverBound.register(StatusPingPacket::new,
                    map(0x01, getMin(), getMax())
            );
            clientBound.register(ClientboundStatusResponse::new,
                    map(0x00, getMin(), getMax())
            );
            clientBound.register(StatusPingPacket::new,
                    map(0x01, getMin(), getMax())
            );
        }
    },
    LOGIN(2) {
        {
            serverBound.register(ServerboundLoginStart::new,
                    map(0x00, getMin(), getMax())
            );
            serverBound.register(ServerboundLoginPluginResponse::new,
                    map(0x02, getMin(), getMax())
            );
            serverBound.register(
                    LoginAcknowledgedPacket::new,
                    map(0x03, V1_20_2, getMax())
            );
            clientBound.register(ClientboundLoginDisconnect::new,
                    map(0x00, getMin(), getMax())
            );
            clientBound.register(ClientboundLoginSuccess::new,
                    map(0x02, getMin(), getMax())
            );
            clientBound.register(ClientboundLoginPluginRequest::new,
                    map(0x04, getMin(), getMax())
            );
        }
    },
    CONFIGURATION(3) {
        {
//            clientBound.register(
//                    PacketPluginMessage::new,
//                    map(0x00, V1_20_2, V1_20_3),
//                    map(0x01, V1_20_5, V1_21_4)
//            );
//            clientBound.register(
//                    PacketDisconnect::new,
//                    map(0x01, V1_20_2, V1_20_3),
//                    map(0x02, V1_20_5, V1_21_4)
//            );
            clientBound.register(
                    FinishConfigurationPacket::new,
                    map(0x02, V1_20_2, V1_20_3),
                    map(0x03, V1_20_5, V1_21_4)
            );
            clientBound.register(
                    KeepAlivePacket::new,
                    map(0x03, V1_20_2, V1_20_3),
                    map(0x04, V1_20_5, V1_21_4)
            );
            clientBound.register(
                    ClientboundRegistryData::new,
                    map(0x05, V1_20_2, V1_20_3),
                    map(0x07, V1_20_5, V1_21_4)
            );

            serverBound.register(ServerboundPlayerLoaded::new,
                    map(0x00, V1_21_4, V1_21_4)
            );
//            serverBound.register(
//                    PacketPluginMessage::new,
//                    map(0x01, V1_20_2, V1_20_3),
//                    map(0x02, V1_20_2, V1_21_4)
//            );
            serverBound.register(
                    FinishConfigurationPacket::new,
                    map(0x02, V1_20_2, V1_20_3),
                    map(0x03, V1_20_5, V1_21_4)
            );
            serverBound.register(
                    KeepAlivePacket::new,
                    map(0x03, V1_20_2, V1_20_3),
                    map(0x04, V1_20_5, V1_21_4)
            );
        }
    },
    PLAY(4) {
        {
            serverBound.register(KeepAlivePacket::new,
                    map(0x00, V1_7_2, V1_8),
                    map(0x0B, V1_9, V1_11_1),
                    map(0x0C, V1_12, V1_12),
                    map(0x0B, V1_12_1, V1_12_2),
                    map(0x0E, V1_13, V1_13_2),
                    map(0x0F, V1_14, V1_15_2),
                    map(0x10, V1_16, V1_16_4),
                    map(0x0F, V1_17, V1_18_2),
                    map(0x11, V1_19, V1_19),
                    map(0x12, V1_19_1, V1_19_1),
                    map(0x11, V1_19_3, V1_19_3),
                    map(0x12, V1_19_4, V1_20),
                    map(0x14, V1_20_2, V1_20_2),
                    map(0x15, V1_20_3, V1_20_3),
                    map(0x18, V1_20_5, V1_21),
                    map(0x1A, V1_21_2, V1_21_4)
            );
            clientBound.register(ClientboundDeclareCommands::new,
                    map(0x11, V1_13, V1_14_4),
                    map(0x12, V1_15, V1_15_2),
                    map(0x11, V1_16, V1_16_1),
                    map(0x10, V1_16_2, V1_16_4),
                    map(0x12, V1_17, V1_18_2),
                    map(0x0F, V1_19, V1_19_1),
                    map(0x0E, V1_19_3, V1_19_3),
                    map(0x10, V1_19_4, V1_20),
                    map(0x11, V1_20_2, V1_21_4)
            );
            clientBound.register(ClientboundJoinGame::new,
                    map(0x01, V1_7_2, V1_8),
                    map(0x23, V1_9, V1_12_2),
                    map(0x25, V1_13, V1_14_4),
                    map(0x26, V1_15, V1_15_2),
                    map(0x25, V1_16, V1_16_1),
                    map(0x24, V1_16_2, V1_16_4),
                    map(0x26, V1_17, V1_18_2),
                    map(0x23, V1_19, V1_19),
                    map(0x25, V1_19_1, V1_19_1),
                    map(0x24, V1_19_3, V1_19_3),
                    map(0x28, V1_19_4, V1_20),
                    map(0x29, V1_20_2, V1_20_3),
                    map(0x2B, V1_20_5, V1_21),
                    map(0x2C, V1_21_2, V1_21_4)
            );
            clientBound.register(ClientboundPlayerAbilities::new,
                    map(0x39, V1_7_2, V1_8),
                    map(0x2B, V1_9, V1_12),
                    map(0x2C, V1_12_1, V1_12_2),
                    map(0x2E, V1_13, V1_13_2),
                    map(0x31, V1_14, V1_14_4),
                    map(0x32, V1_15, V1_15_2),
                    map(0x31, V1_16, V1_16_1),
                    map(0x30, V1_16_2, V1_16_4),
                    map(0x32, V1_17, V1_18_2),
                    map(0x2F, V1_19, V1_19),
                    map(0x31, V1_19_1, V1_19_1),
                    map(0x30, V1_19_3, V1_19_3),
                    map(0x34, V1_19_4, V1_20),
                    map(0x36, V1_20_2, V1_20_3),
                    map(0x38, V1_20_5, V1_21),
                    map(0x3A, V1_21_2, V1_21_4)
            );
            clientBound.register(ClientboundPlayerPositionAndLook::new,
                    map(0x08, V1_7_2, V1_8),
                    map(0x2E, V1_9, V1_12),
                    map(0x2F, V1_12_1, V1_12_2),
                    map(0x32, V1_13, V1_13_2),
                    map(0x35, V1_14, V1_14_4),
                    map(0x36, V1_15, V1_15_2),
                    map(0x35, V1_16, V1_16_1),
                    map(0x34, V1_16_2, V1_16_4),
                    map(0x38, V1_17, V1_18_2),
                    map(0x36, V1_19, V1_19),
                    map(0x39, V1_19_1, V1_19_1),
                    map(0x38, V1_19_3, V1_19_3),
                    map(0x3C, V1_19_4, V1_20),
                    map(0x3E, V1_20_2, V1_20_3),
                    map(0x40, V1_20_5, V1_21),
                    map(0x42, V1_21_2, V1_21_4)
            );
            clientBound.register(KeepAlivePacket::new,
                    map(0x00, V1_7_2, V1_8),
                    map(0x1F, V1_9, V1_12_2),
                    map(0x21, V1_13, V1_13_2),
                    map(0x20, V1_14, V1_14_4),
                    map(0x21, V1_15, V1_15_2),
                    map(0x20, V1_16, V1_16_1),
                    map(0x1F, V1_16_2, V1_16_4),
                    map(0x21, V1_17, V1_18_2),
                    map(0x1E, V1_19, V1_19),
                    map(0x20, V1_19_1, V1_19_1),
                    map(0x1F, V1_19_3, V1_19_3),
                    map(0x23, V1_19_4, V1_20),
                    map(0x24, V1_20_2, V1_20_3),
                    map(0x26, V1_20_5, V1_21),
                    map(0x27, V1_21_2, V1_21_4)
            );
            clientBound.register(ClientboundSpawnPosition::new,
                    map(0x4C, V1_19_3, V1_19_3),
                    map(0x50, V1_19_4, V1_20),
                    map(0x52, V1_20_2, V1_20_2),
                    map(0x54, V1_20_3, V1_20_3),
                    map(0x56, V1_20_5, V1_21),
                    map(0x5B, V1_21_2, V1_21_4)
            );
            clientBound.register(ClientboundGameEvent::new,
                    map(0x20, V1_20_3, V1_20_3),
                    map(0x22, V1_20_5, V1_21),
                    map(0x23, V1_21_2, V1_21_4)
            );
            clientBound.register(ClientboundEmptyChunk::new,
                    map(0x25, V1_20_3, V1_20_3),
                    map(0x27, V1_20_5, V1_21),
                    map(0x28, V1_21_2, V1_21_4)
            );
        }
    };

    private static final Map<Integer, State> STATE_BY_ID = new HashMap<>();

    static {
        for (State registry : values()) {
            STATE_BY_ID.put(registry.stateId, registry);
        }
    }

    private final int stateId;
    public final ProtocolMappings serverBound = new ProtocolMappings();
    public final ProtocolMappings clientBound = new ProtocolMappings();

    State(int stateId) {
        this.stateId = stateId;
    }

    public static State getById(int stateId) {
        return STATE_BY_ID.get(stateId);
    }

    public static class ProtocolMappings {

        private final Map<Version, PacketRegistry> registry = new HashMap<>();

        public PacketRegistry getRegistry(Version version) {
            return registry.getOrDefault(version, registry.get(getMin()));
        }

        public void register(Supplier<?> packet, Mapping... mappings) {
            for (Mapping mapping : mappings) {
                for (Version ver : getRange(mapping)) {
                    PacketRegistry reg = registry.computeIfAbsent(ver, PacketRegistry::new);
                    reg.register(mapping.packetId, packet);
                }
            }
        }

        private Collection<Version> getRange(Mapping mapping) {
            Version from = mapping.from;
            Version curr = mapping.to;

            if (curr == from)
                return Collections.singletonList(from);

            List<Version> versions = new LinkedList<>();

            while (curr != from) {
                versions.add(curr);
                curr = curr.previous();
            }

            versions.add(from);

            return versions;
        }

    }

    public static class PacketRegistry {

        @Getter
        private final Version version;
        private final Map<Integer, Supplier<?>> packetsById = new HashMap<>();
        private final Map<Class<?>, Integer> packetIdByClass = new HashMap<>();

        public PacketRegistry(Version version) {
            this.version = version;
        }

        public Packet getPacket(int packetId) {
            Supplier<?> supplier = packetsById.get(packetId);
            return supplier == null ? null : (Packet) supplier.get();
        }

        public ServerboundPacket getServerboundPacket(int packetId) {
            Supplier<?> supplier = packetsById.get(packetId);
            return supplier == null ? null : (ServerboundPacket) supplier.get();
        }

        public int getPacketId(Class<?> packetClass) {
            return packetIdByClass.getOrDefault(packetClass, -1);
        }

        public void register(int packetId, Supplier<?> supplier) {
            packetsById.put(packetId, supplier);
            packetIdByClass.put(supplier.get().getClass(), packetId);
        }

    }

    private record Mapping(int packetId, Version from, Version to) {
    }

    /**
     * Map packet id to version range
     *
     * @param packetId Packet id
     * @param from     Minimal version (include)
     * @param to       Last version (include)
     * @return Created mapping
     */
    private static Mapping map(int packetId, Version from, Version to) {
        return new Mapping(packetId, from, to);
    }
}
