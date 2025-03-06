package me.rochblondiaux.ultralimbo.entity.metadata;

import java.util.function.BiConsumer;
import java.util.function.Function;

import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;


public record EntityDataType<T>(String name, int id, Function<ByteMessage, T> dataDeserializer,
                                BiConsumer<ByteMessage, Object> dataSerializer) {

}