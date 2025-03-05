package me.rochblondiaux.ultralimbo.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import me.rochblondiaux.ultralimbo.command.CommandSender;
import me.rochblondiaux.ultralimbo.network.connection.ClientConnection;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundBossBar;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundChatMessage;
import me.rochblondiaux.ultralimbo.network.protocol.packets.play.client.ClientboundCloseContainer;
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
    private int openInventoryId;
    private final Map<UUID, BossBar> bossBars = new HashMap<>();

    public Player(ClientConnection connection) {
        this.entityId = 0;
        this.uniqueId = connection.uniqueId();
        this.name = connection.username();
        this.connection = connection;
        this.entityIdCounter = new AtomicInteger(1);
        this.gameMode = GameMode.SURVIVAL;
        this.openInventoryId = -1;
    }

    public void closeInventory() {
        if (openInventoryId < 0)
            return;

        this.connection.sendPacket(new ClientboundCloseContainer(openInventoryId));
    }

    @Override
    public void sendMessage(String message) {
        this.sendMessage(Component.text(message));
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        this.connection.sendPacket(new ClientboundChatMessage(
                UUID.randomUUID(),
                ClientboundChatMessage.LegacyPosition.CHAT,
                message
        ));
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        this.connection.sendPacket(new ClientboundChatMessage(
                UUID.randomUUID(),
                ClientboundChatMessage.LegacyPosition.ACTION_BAR,
                message
        ));
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
        UUID uniqueId = UUID.randomUUID();
        this.bossBars.put(uniqueId, bar);

        this.connection.sendPacket(new ClientboundBossBar(uniqueId, ClientboundBossBar.Action.ADD, bar));
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        this.findBossBarUniqueId(bar)
                .ifPresent(uniqueId -> {
                    this.bossBars.remove(uniqueId);
                    this.connection.sendPacket(new ClientboundBossBar(uniqueId, ClientboundBossBar.Action.REMOVE, bar));
                });
    }

    public int currentEntityId() {
        return this.entityIdCounter.get();
    }

    public int nextEntityId() {
        return this.entityIdCounter.incrementAndGet();
    }

    private Optional<UUID> findBossBarUniqueId(BossBar bar) {
        return this.bossBars.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(bar))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
