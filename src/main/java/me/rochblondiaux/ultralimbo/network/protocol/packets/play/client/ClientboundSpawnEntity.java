package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.entity.EntityType;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import me.rochblondiaux.ultralimbo.registry.Registries;
import me.rochblondiaux.ultralimbo.utils.Location;
import me.rochblondiaux.ultralimbo.utils.NumberConversions;
import me.rochblondiaux.ultralimbo.utils.Vector;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundSpawnEntity implements ClientboundPacket {

    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private static final double VELOCITY_FACTOR = 8000.0;

    private int id;
    private UUID uniqueId;
    private Location location;
    private EntityType type;
    private float headYaw;
    private int data;
    private Vector velocity;

    public ClientboundSpawnEntity(int id, UUID uniqueId, Location location, EntityType type, float headYaw) {
        this(id, uniqueId, location, type, headYaw, 0, new Vector());
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(this.id);
        msg.writeUuid(this.uniqueId);
        msg.writeVarInt(Registries.ENTITY_TYPE.get(this.type, version).orElseThrow(() -> new IllegalArgumentException("Unknown entity type " + this.type)).intValue());

        // Location
        msg.writeDouble(this.location.x());
        msg.writeDouble(this.location.y());
        msg.writeDouble(this.location.z());
        msg.writeByte(NumberConversions.floor(this.location.pitch() * ROTATION_FACTOR));
        msg.writeByte(NumberConversions.floor(this.location.yaw() * ROTATION_FACTOR));
        msg.writeByte(NumberConversions.floor(this.headYaw * ROTATION_FACTOR));

        // Data
        msg.writeVarInt(this.data);

        // Velocity
        msg.writeShort((int) (this.velocity.x() * VELOCITY_FACTOR));
        msg.writeShort((int) (this.velocity.y() * VELOCITY_FACTOR));
        msg.writeShort((int) (this.velocity.z() * VELOCITY_FACTOR));
    }
}
