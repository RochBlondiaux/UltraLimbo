package me.rochblondiaux.ultralimbo.entity.metadata;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import net.kyori.adventure.text.Component;

public class EntityDataTypes {

    private static final Map<String, EntityDataType<?>> ENTITY_DATA_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, EntityDataType<?>>> ENTITY_DATA_TYPE_ID_MAP = new HashMap<>();

    public static final EntityDataType<Byte> BYTE = define("byte", ByteMessage::readByte, (e, v) -> e.writeByte(v));

    public static final EntityDataType<Integer> INT = define("int", ByteMessage::readInt, ByteMessage::writeVarInt);

    public static final EntityDataType<Long> LONG = define("long", ByteMessage::readLongLE, ByteMessage::writeLongLE);

    public static final EntityDataType<Float> FLOAT = define("float", ByteMessage::readFloat, ByteMessage::writeFloat);

    public static final EntityDataType<String> STRING = define("string", ByteMessage::readString, ByteMessage::writeString);

    public static final EntityDataType<Component> COMPONENT = define("component", ByteMessage::readComponent, ByteMessage::writeComponent);

    public static Collection<EntityDataType<?>> values() {
        return Collections.unmodifiableCollection(ENTITY_DATA_TYPE_MAP.values());
    }

    public static EntityDataType<?> getByName(String name) {
        return ENTITY_DATA_TYPE_MAP.get(name);
    }

    public static <T> EntityDataType<T> define(String name, Function<ByteMessage, T> deserializer, BiConsumer<ByteMessage, T> serializer) {
       try {
           DataType dataType = DataType.fromString(name);

           EntityDataType<T> type = new EntityDataType<>(name, dataType.ordinal(), deserializer,
                   (BiConsumer<ByteMessage, Object>) serializer);
           ENTITY_DATA_TYPE_MAP.put(type.name(), type);
           Map<Integer, EntityDataType<?>> typeIdMap = ENTITY_DATA_TYPE_ID_MAP
                   .computeIfAbsent((byte) dataType.ordinal(), k -> new HashMap<>());
           typeIdMap.put(type.id(), type);
           return type;
       }catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    private static <T> Function<ByteMessage, T> readIntDeserializer() {
        return (ByteMessage wrapper) -> (T) ((Object) wrapper.readVarInt());
    }

    private static <T> BiConsumer<ByteMessage, T> writeIntSerializer() {
        return (ByteMessage wrapper, T value) -> {
            int output = 0;
            if (value instanceof Byte) {
                output = ((Byte) value).intValue();
            } else if (value instanceof Short) {
                output = ((Short) value).intValue();
            } else if (value instanceof Integer) {
                output = (Integer) value;
            } else if (value instanceof Long) {
                output = ((Long) value).intValue();
            }
            wrapper.writeVarInt(output);
        };
    }

}
