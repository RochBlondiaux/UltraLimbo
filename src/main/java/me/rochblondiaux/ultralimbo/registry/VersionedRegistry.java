package me.rochblondiaux.ultralimbo.registry;

import java.util.Map;
import java.util.Optional;

import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

public interface VersionedRegistry<K, V> {

    Optional<V> get(K key, Version version);

    void register(K key, V value, Version version);

    void unregister(K key, Version version);

    void load(String path);

    Map<K, V> getAll(Version version);

}
