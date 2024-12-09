package me.rochblondiaux.ultralimbo.configuration.serializer;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ComponentSerializer implements TypeSerializer<Component> {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static final ComponentSerializer INSTANCE = new ComponentSerializer();

    @Override
    public Component deserialize(Type type, ConfigurationNode node) {
        String value = node.getString();
        if (value == null)
            return null;
        return MINI_MESSAGE.deserialize(value);
    }

    @Override
    public void serialize(Type type, @Nullable Component obj, ConfigurationNode node) {
        if (obj == null)
            return;

        String value = MINI_MESSAGE.serialize(obj);

        try {
            node.set(value);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
}
