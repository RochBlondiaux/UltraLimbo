package me.rochblondiaux.ultralimbo.world;

import lombok.Getter;
import me.rochblondiaux.ultralimbo.utils.Vector;

@Getter
public enum BlockFace {
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    WEST_NORTH_WEST(WEST, NORTH_WEST),
    NORTH_NORTH_WEST(NORTH, NORTH_WEST),
    NORTH_NORTH_EAST(NORTH, NORTH_EAST),
    EAST_NORTH_EAST(EAST, NORTH_EAST),
    EAST_SOUTH_EAST(EAST, SOUTH_EAST),
    SOUTH_SOUTH_EAST(SOUTH, SOUTH_EAST),
    SOUTH_SOUTH_WEST(SOUTH, SOUTH_WEST),
    WEST_SOUTH_WEST(WEST, SOUTH_WEST),
    SELF(0, 0, 0);

    private final int modX;
    private final int modY;
    private final int modZ;

    BlockFace(final int modX, final int modY, final int modZ) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    BlockFace(final BlockFace face1, final BlockFace face2) {
        this.modX = face1.modX() + face2.modX();
        this.modY = face1.modY() + face2.modY();
        this.modZ = face1.modZ() + face2.modZ();
    }

    public Vector getDirection() {
        Vector direction = new Vector(modX, modY, modZ);
        if (modX != 0 || modY != 0 || modZ != 0)
            direction.normalize();
        return direction;
    }

    public boolean isCartesian() {
        return switch (this) {
            case NORTH, SOUTH, EAST, WEST, UP, DOWN -> true;
            default -> false;
        };
    }

    public BlockFace getOppositeFace() {
        return switch (this) {
            case NORTH -> BlockFace.SOUTH;
            case SOUTH -> BlockFace.NORTH;
            case EAST -> BlockFace.WEST;
            case WEST -> BlockFace.EAST;
            case UP -> BlockFace.DOWN;
            case DOWN -> BlockFace.UP;
            case NORTH_EAST -> BlockFace.SOUTH_WEST;
            case NORTH_WEST -> BlockFace.SOUTH_EAST;
            case SOUTH_EAST -> BlockFace.NORTH_WEST;
            case SOUTH_WEST -> BlockFace.NORTH_EAST;
            case WEST_NORTH_WEST -> BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_WEST -> BlockFace.SOUTH_SOUTH_EAST;
            case NORTH_NORTH_EAST -> BlockFace.SOUTH_SOUTH_WEST;
            case EAST_NORTH_EAST -> BlockFace.WEST_SOUTH_WEST;
            case EAST_SOUTH_EAST -> BlockFace.WEST_NORTH_WEST;
            case SOUTH_SOUTH_EAST -> BlockFace.NORTH_NORTH_WEST;
            case SOUTH_SOUTH_WEST -> BlockFace.NORTH_NORTH_EAST;
            case WEST_SOUTH_WEST -> BlockFace.EAST_NORTH_EAST;
            case SELF -> BlockFace.SELF;
        };

    }
}
