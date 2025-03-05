package me.rochblondiaux.ultralimbo.network.protocol.packets.status.client;


import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundStatusResponse implements ClientboundPacket {

    private static String CACHED_RESPONSE;

    private int protocol;
    private String version;
    private Component description;
    private int onlinePlayers;
    private int maximumPlayers;

    @Override
    public void encode(ByteMessage msg, Version version) {
        String motd = CACHED_RESPONSE == null ? CACHED_RESPONSE = buildMotd() : CACHED_RESPONSE;
        msg.writeString(motd);
    }

    private String buildMotd() {
        JsonObject jsonObject = new JsonObject();

        JsonObject versionObject = new JsonObject();
        versionObject.addProperty("name", this.version);
        versionObject.addProperty("protocol", this.protocol);
        jsonObject.add("version", versionObject);

        JsonObject playersObject = new JsonObject();
        playersObject.addProperty("max", this.maximumPlayers);
        playersObject.addProperty("online", this.onlinePlayers);
        jsonObject.add("players", playersObject);

        jsonObject.addProperty("description", "%MOTD%");

        return new Gson().toJson(jsonObject).replace("\"%MOTD%\"", GsonComponentSerializer.gson().serialize(description));
    }
}
