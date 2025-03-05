package me.rochblondiaux.ultralimbo.player;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import me.rochblondiaux.ultralimbo.command.CommandSender;
import me.rochblondiaux.ultralimbo.network.connection.ClientConnection;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

@Getter
public class Player implements Audience, CommandSender {

    private final int entityId;
    private final UUID uniqueId;
    private final String name;
    private final ClientConnection connection;
    private final AtomicInteger entityIdCounter;
    private GameMode gameMode;

    public Player(ClientConnection connection) {
        this.entityId = 0;
        this.uniqueId = connection.uniqueId();
        this.name = connection.username();
        this.connection = connection;
        this.entityIdCounter = new AtomicInteger(1);
        this.gameMode = GameMode.SURVIVAL;
    }


    @Override
    public void sendMessage(String message) {
        // TODO: implement
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        // TODO: implement
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        // TODO: implement
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        // TODO: implement
    }

    @Override
    public void showTitle(@NotNull Title title) {
        // TODO: implement
    }

    @Override
    public void sendPlayerListFooter(@NotNull Component footer) {
        // TODO: implement
    }

    @Override
    public void sendPlayerListHeader(@NotNull Component header) {
        // TODO: implement
    }

    @Override
    public void resetTitle() {
        // TODO: implement
    }

    @Override
    public void clearTitle() {
        // TODO: implement
    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {
        // TODO: implement
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        // TODO: implement
    }

    public int currentEntityId() {
        return this.entityIdCounter.get();
    }

    public int nextEntityId() {
        return this.entityIdCounter.incrementAndGet();
    }
}
