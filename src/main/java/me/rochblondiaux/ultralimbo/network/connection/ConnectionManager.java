package me.rochblondiaux.ultralimbo.network.connection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jetbrains.annotations.Unmodifiable;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.Limbo;

@Log4j2
@RequiredArgsConstructor
public class ConnectionManager {

    private final Limbo app;
    private final List<ClientConnection> connections = new CopyOnWriteArrayList<>();

    public void add(ClientConnection connection) {
        this.connections.add(connection);
        log.debug("Added connection {}", connection.uniqueId());
    }

    public void remove(ClientConnection connection) {
        this.connections.remove(connection);
        log.debug("Removed connection {}", connection.uniqueId());

        this.app.playerManager().unregister(connection.uniqueId());
    }

    public int size() {
        return connections.size();
    }

    @Unmodifiable
    public List<ClientConnection> connections() {
        return List.copyOf(connections);
    }
}
