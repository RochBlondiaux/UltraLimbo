package me.rochblondiaux.ultralimbo.configuration.implementation;

import java.net.SocketAddress;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public record ServerConfiguration(@Setting("bind") SocketAddress address, int maxPlayers, Motd motd) {

    @ConfigSerializable
    public record Motd(String version, Component description, int protocol) {
    }
}
