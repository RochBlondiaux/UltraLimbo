package me.rochblondiaux.ultralimbo.configuration.implementation;

import java.net.SocketAddress;
import java.util.Map;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import me.rochblondiaux.ultralimbo.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@ConfigSerializable
public record ServerConfiguration(@Setting("bind") SocketAddress address, int maxPlayers, Motd motd, Map<String, String> messages) {

    @ConfigSerializable
    public record Motd(String version, Component description, int protocol) {
    }

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public String raw(String key) {
        String message = this.messages.get(key);
        return message == null ? key : message;
    }

    public Component component(String key, TagResolver... resolvers) {
        String raw = this.raw(key);
        if (raw.equals(key))
            return Component.text(raw);
        return MINI_MESSAGE.deserialize(raw, resolvers);
    }

    public void message(CommandSender sender, String key, TagResolver... resolvers) {
        sender.sendMessage(this.component(key, resolvers));
    }
}
