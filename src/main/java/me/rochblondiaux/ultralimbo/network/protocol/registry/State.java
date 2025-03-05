package me.rochblondiaux.ultralimbo.network.protocol.registry;

import java.util.*;
import java.util.function.Supplier;

import lombok.Getter;
import me.rochblondiaux.ultralimbo.network.protocol.Packet;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.ServerboundHandshake;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.StatusPingPacket;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.client.ClientboundStatusResponse;
import me.rochblondiaux.ultralimbo.network.protocol.packets.status.server.ServerboundStatusRequest;

public enum State {
    HANDSHAKING(0) {
        {
            serverBound.register(ServerboundHandshake::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
        }
    },
    STATUS(1) {
        {
            serverBound.register(ServerboundStatusRequest::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
            serverBound.register(StatusPingPacket::new,
                    map(0x01, Version.getMin(), Version.getMax())
            );
            clientBound.register(ClientboundStatusResponse::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
            clientBound.register(StatusPingPacket::new,
                    map(0x01, Version.getMin(), Version.getMax())
            );
        }
    },
    LOGIN(2) {
        {
//            serverBound.register(PacketLoginStart::new,
//                    map(0x00, Version.getMin(), Version.getMax())
//            );
//            serverBound.register(PacketLoginPluginResponse::new,
//                    map(0x02, Version.getMin(), Version.getMax())
//            );
//            serverBound.register(
//                    PacketLoginAcknowledged::new,
//                    map(0x03, V1_20_2, Version.getMax())
//            );
//            clientBound.register(PacketDisconnect::new,
//                    map(0x00, Version.getMin(), Version.getMax())
//            );
//            clientBound.register(PacketLoginSuccess::new,
//                    map(0x02, Version.getMin(), Version.getMax())
//            );
//            clientBound.register(PacketLoginPluginRequest::new,
//                    map(0x04, Version.getMin(), Version.getMax())
//            );
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
//            clientBound.register(
//                    PacketFinishConfiguration::new,
//                    map(0x02, V1_20_2, V1_20_3),
//                    map(0x03, V1_20_5, V1_21_4)
//            );
//            clientBound.register(
//                    PacketKeepAlive::new,
//                    map(0x03, V1_20_2, V1_20_3),
//                    map(0x04, V1_20_5, V1_21_4)
//            );
//            clientBound.register(
//                    PacketRegistryData::new,
//                    map(0x05, V1_20_2, V1_20_3),
//                    map(0x07, V1_20_5, V1_21_4)
//            );
//
//            serverBound.register(PacketPlayerLoaded::new,
//                    map(0x00, V1_21_4, V1_21_4)
//            );
//            serverBound.register(
//                    PacketPluginMessage::new,
//                    map(0x01, V1_20_2, V1_20_3),
//                    map(0x02, V1_20_2, V1_21_4)
//            );
//            serverBound.register(
//                    PacketFinishConfiguration::new,
//                    map(0x02, V1_20_2, V1_20_3),
//                    map(0x03, V1_20_5, V1_21_4)
//            );
//            serverBound.register(
//                    PacketKeepAlive::new,
//                    map(0x03, V1_20_2, V1_20_3),
//                    map(0x04, V1_20_5, V1_21_4)
//            );
        }
    },
    PLAY(4) {
        {


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
            return registry.getOrDefault(version, registry.get(Version.getMin()));
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
