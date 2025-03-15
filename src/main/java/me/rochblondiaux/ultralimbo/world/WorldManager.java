package me.rochblondiaux.ultralimbo.world;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.utils.SchematicUtils;
import net.kyori.adventure.key.Key;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

@RequiredArgsConstructor
@Getter
@Log4j2
public class WorldManager {

    private final Limbo app;
    private World world;

    public void load() {
        log.info("Loading world...");

        // Environment
        Environment environment = Environment.findByKey(Key.key("minecraft", app.configuration().dimension()))
                .orElseThrow(() -> new RuntimeException("Invalid dimension configuration"));
        log.info("Environment: {}", environment.key());

        // Default schematic
        Path schematicPath = app.dataFolder().resolve("world.schem");
        if (!Files.exists(schematicPath)) {
            log.info("Creating default world schematic...");
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("world.schem")) {
                if (inputStream == null)
                    throw new RuntimeException("Failed to load default world schematic");
                Files.copy(inputStream, schematicPath);
                log.info("Copied default world schematic!");
            } catch (Exception e) {
                throw new RuntimeException("Failed to create default world schematic", e);
            }
        }

        // Load world
        try {
            this.world = SchematicUtils.parseWorld(
                    "world",
                    environment,
                    (CompoundTag) NBTUtil.read(schematicPath.toFile()).getTag()
            );

            log.info("World loaded!");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load world", e);
        }
    }
}
