package me.rochblondiaux.ultralimbo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.configuration.YmlConfiguration;
import me.rochblondiaux.ultralimbo.configuration.implementation.ServerConfiguration;

@Getter
@Log4j2
public class Limbo {

    private final Path dataFolder;

    // Configuration
    private ServerConfiguration configuration;

    // State
    private final AtomicBoolean running = new AtomicBoolean(true);

    public Limbo() {
        this.dataFolder = Paths.get("").toAbsolutePath();

        // Start the app
        this.start();
    }

    public void start() {
        log.info("Starting UltraLimbo...");

        // Load the configuration
        log.info("Loading configuration...");
        try {
            YmlConfiguration configuration = new YmlConfiguration(this.dataFolder.resolve("config.yml"), "configuration/config.yml");
            configuration.load();
            this.configuration = configuration.get(ServerConfiguration.class);
        } catch (Exception e) {
            log.error("An error occurred while loading the configuration", e);
            System.exit(1);
            return;
        }
        log.info("Configuration loaded");
    }
}
