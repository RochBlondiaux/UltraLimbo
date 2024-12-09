package me.rochblondiaux.ultralimbo.configuration;

import java.io.InputStream;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import lombok.Getter;
import me.rochblondiaux.ultralimbo.configuration.serializer.ComponentSerializer;
import me.rochblondiaux.ultralimbo.configuration.serializer.SocketAddressSerializer;
import me.rochblondiaux.ultralimbo.exception.configuration.ConfigurationLoadException;
import me.rochblondiaux.ultralimbo.exception.configuration.ConfigurationSaveException;
import net.kyori.adventure.text.Component;

@Getter
public class YmlConfiguration {

    private final Path path;
    private final @Nullable String defaultPath;
    private final YamlConfigurationLoader loader;
    protected @Nullable CommentedConfigurationNode root;

    public YmlConfiguration(Path path, @Nullable String defaultPath, YamlConfigurationLoader loader) {
        this.path = path;
        this.defaultPath = defaultPath;
        this.loader = loader;
    }

    public YmlConfiguration(Path path, YamlConfigurationLoader loader) {
        this(path, null, loader);
    }

    public YmlConfiguration(Path path, @NotNull String defaultPath) {
        this(path, defaultPath, YamlConfigurationLoader.builder()
                .path(path)
                .defaultOptions(configurationOptions ->
                        configurationOptions.serializers(builder -> builder.register(SocketAddress.class, SocketAddressSerializer.INSTANCE)
                                .register(Component.class, ComponentSerializer.INSTANCE)))
                .build());
    }

    public void load() throws ConfigurationLoadException {
        this.createDefault();

        try {
            this.root = this.loader.load();
        } catch (Exception e) {
            throw new ConfigurationLoadException("Failed to load configuration", e);
        }
    }

    public <T> T get(Class<T> type, @NotNull Object... path) {
        if (this.root == null)
            throw new ConfigurationLoadException("Root node is null");

        try {
            return this.root.node(path).get(type);
        } catch (SerializationException e) {
            throw new ConfigurationLoadException("Failed to get value", e);
        }
    }

    public void save() throws ConfigurationSaveException {
        if (this.root == null)
            throw new ConfigurationSaveException("Root node is null");

        try {
            this.loader.save(this.root);
        } catch (Exception e) {
            throw new ConfigurationSaveException("Failed to save configuration", e);
        }
    }

    private void createDefault() {
        if (this.defaultPath == null || Files.exists(this.path))
            return;

        try {
            Files.createDirectories(this.path.getParent());
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(this.defaultPath)) {
                if (inputStream == null)
                    throw new ConfigurationLoadException("Default configuration not found: " + this.defaultPath);
                Files.copy(inputStream, this.path);
            }
        } catch (Exception e) {
            throw new ConfigurationLoadException("Failed to create default configuration", e);
        }
    }
}
