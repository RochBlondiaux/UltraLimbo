package me.rochblondiaux.ultralimbo.entity;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.Unmodifiable;

import lombok.Getter;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundDestroyEntities;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundSpawnEntity;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundTeleportEntity;
import me.rochblondiaux.ultralimbo.utils.Location;

@Getter
public class Entity {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger();

    private final int entityId;
    private final UUID uniqueId;
    private final EntityType type;
    private Location location;
    private boolean onGround;
    private boolean alive;

    public Entity(UUID uniqueId, EntityType type) {
        this.entityId = ID_COUNTER.getAndIncrement();
        this.uniqueId = uniqueId;
        this.type = type;
    }


    public void spawn(Location location) {
        if (this.alive)
            throw new IllegalStateException("Entity is already spawned");
        this.location = location;

        ClientboundSpawnEntity spawnPacket = new ClientboundSpawnEntity(
                this.entityId,
                this.uniqueId,
                location,
                this.type,
                0
        );
        // TODO: Send packet to all players

        this.alive = true;
    }

    public void teleport(Location location) {
        if (!this.alive)
            throw new IllegalStateException("Entity is not spawned");

        ClientboundTeleportEntity teleportPacket = new ClientboundTeleportEntity(
                this.entityId,
                location,
                this.onGround
        );
        // TODO: Use relative movement packet
        // TODO: Send packet to all players
        this.location = location;
    }

    public void destroy() {
        if (!this.alive)
            throw new IllegalStateException("Entity is not spawned");

        ClientboundDestroyEntities destroyPacket = new ClientboundDestroyEntities(this.entityId);
        // TODO: Send packet to all players
        
        this.alive = false;
    }

    @Unmodifiable
    public Location location() {
        return this.location.clone();
    }
}
