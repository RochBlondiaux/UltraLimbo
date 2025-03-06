package me.rochblondiaux.ultralimbo.registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Unmodifiable;

import com.google.gson.Gson;

import io.leangen.geantyref.TypeToken;
import me.rochblondiaux.ultralimbo.entity.EntityType;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.key.Key;

public class EntityTypeRegistry implements VersionedRegistry<Key, Integer> {

    private final Map<Version, Map<Key, Integer>> entries = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    @Override
    public Optional<Integer> get(Key key, Version version) {
        Map<Key, Integer> map = entries.get(version);
        if (map == null)
            return Optional.empty();
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public void register(Key key, Integer value, Version version) {
        Map<Key, Integer> map = entries.computeIfAbsent(version, k -> new HashMap<>());
        map.put(key, value);
    }

    @Override
    public void unregister(Key key, Version version) {
        Map<Key, Integer> map = entries.get(version);
        if (map != null)
            map.remove(key);
    }

    @Override
    public void load(String path) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null)
                throw new IllegalArgumentException("Resource not found: " + path);

            TypeToken<Map<String, Map<String, Integer>>> typeToken = new TypeToken<>() {
            };
            Map<String, Map<String, Integer>> data = gson.fromJson(new String(inputStream.readAllBytes()), typeToken.getType());
            data.forEach((rawVersion, map) -> {
                Version version = Version.valueOf(rawVersion);
                Map<Key, Integer> versionMap = entries.computeIfAbsent(version, k -> new HashMap<>(map.size()));
                map.forEach((key, value) -> versionMap.put(Key.key("minecraft", key), value));
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load registry from " + path, e);
        }
    }

    public Optional<Integer> get(EntityType entityType, Version version) {
        return get(entityType.key(), version);
    }

    @Override
    @Unmodifiable
    public Map<Key, Integer> getAll(Version version) {
        return Map.copyOf(entries.computeIfAbsent(version, k -> new HashMap<>()));
    }

}
