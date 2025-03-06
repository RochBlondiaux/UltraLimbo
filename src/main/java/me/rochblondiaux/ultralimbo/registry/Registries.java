package me.rochblondiaux.ultralimbo.registry;

public class Registries {

    public static final EntityTypeRegistry ENTITY_TYPE = new EntityTypeRegistry();

    public static void init() {
        ENTITY_TYPE.load("minecraft/mappings/entity_type_mappings.json");
    }
}
