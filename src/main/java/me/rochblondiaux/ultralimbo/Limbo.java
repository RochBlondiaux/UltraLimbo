package me.rochblondiaux.ultralimbo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.util.ResourceLeakDetector;
import lombok.Getter;
import me.rochblondiaux.ultralimbo.command.CommandManager;
import me.rochblondiaux.ultralimbo.command.implementation.HelpCommand;
import me.rochblondiaux.ultralimbo.command.implementation.StopCommand;
import me.rochblondiaux.ultralimbo.configuration.YmlConfiguration;
import me.rochblondiaux.ultralimbo.configuration.implementation.ServerConfiguration;
import me.rochblondiaux.ultralimbo.console.LimboConsole;
import me.rochblondiaux.ultralimbo.network.NetworkManager;
import me.rochblondiaux.ultralimbo.network.connection.ConnectionManager;

@Getter
public class Limbo {

    private final Path dataFolder;
    private final Logger logger;

    // Configuration
    private ServerConfiguration configuration;

    // Console & commands
    private CommandManager commands;
    private LimboConsole console;

    // Managers
    private ConnectionManager connections;
    private NetworkManager networkManager;

    // Server
    private LimboServer server;

    // State
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicBoolean stopped = new AtomicBoolean(false);

    public Limbo() {
        this.dataFolder = Paths.get("").toAbsolutePath();
        this.logger = LogManager.getLogger("UltraLimbo");

        // Start the app
        this.start();
    }

    public void start() {
        this.logger.info("Starting UltraLimbo...");
        long start = System.currentTimeMillis();

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        // Load the configuration
        this.logger.info("Loading configuration...");
        try {
            YmlConfiguration configuration = new YmlConfiguration(this.dataFolder.resolve("config.yml"), "configuration/config.yml");
            configuration.load();
            this.configuration = configuration.get(ServerConfiguration.class);
        } catch (Exception e) {
            this.logger.error("An error occurred while loading the configuration", e);
            System.exit(1);
            return;
        }
        this.logger.info("Configuration loaded");

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop, "Shutdown Thread"));

        // Connections
        this.connections = new ConnectionManager();
        this.networkManager = new NetworkManager(this);

        // Commands
        this.commands = new CommandManager(this);
        this.commands.register(new StopCommand(this));
        this.commands.register(new HelpCommand(this));

        // Console
        this.console = new LimboConsole(this);

        // Server
        this.server = new LimboServer(this);
        this.server.start();

        this.logger.info("UltraLimbo started in {}ms", System.currentTimeMillis() - start);

        // Start console
        this.console.start();
    }

    public void halt() {
        this.stopped.set(true);
        this.stop();
    }

    public void stop() {
        this.logger.info("Stopping UltraLimbo...");
        long start = System.currentTimeMillis();

        // Server
        this.server.stop();

        this.logger.info("UltraLimbo stopped in {}ms", System.currentTimeMillis() - start);
    }

    public boolean isRunning() {
        return this.running.get();
    }

    public boolean isStopped() {
        return this.stopped.get();
    }
}
