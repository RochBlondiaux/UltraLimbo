package me.rochblondiaux.ultralimbo.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserProfile {

    private UUID uuid;
    private String name;
    private List<TextureProperty> textureProperties;

    public UserProfile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        textureProperties = new ArrayList<>();
    }

    public UserProfile(UUID uuid, String name, List<TextureProperty> textureProperties) {
        this.uuid = uuid;
        this.name = name;
        this.textureProperties = textureProperties;
    }
}
