package me.rochblondiaux.ultralimbo.network.protocol;

import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@FunctionalInterface
public interface MetadataWriter {

    void writeData(ByteMessage message, Version version);

}
