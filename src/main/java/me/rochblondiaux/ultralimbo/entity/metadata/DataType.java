package me.rochblondiaux.ultralimbo.entity.metadata;

public enum DataType {
    BYTE,
    INT,
    LONG,
    FLOAT,
    STRING,
    COMPONENT,
    OPTIONAL_COMPONENT,
    ITEM_STACK,
    BOOLEAN,
    ROTATION,
    BLOCK_POSITION,
    OPTIONAL_BLOCK_POSITION,
    BLOCK_FACE,
    OPTIONAL_BLOCK_FACE,
    NBT,
    PARTICLE,
    PARTICLES,
    VILLAGER_DATA,
    OPTIONAL_INT,
    ENTITY_POSE,
    CAT_VARIANT_TYPE,
    WOLF_VARIANT_TYPE,
    FROG_VARIANT_TYPE,
    OPTIONAL_GLOBAL_POSITION,
    PAINTING_VARIANT_TYPE,
    SNIFFER_STATE,
    ARMADILLO_STATE,
    VECTOR3F,
    QUATERNION;

    public static DataType fromString(String name) {
        return DataType.valueOf(name.toUpperCase());
    }
}
