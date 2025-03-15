package me.rochblondiaux.ultralimbo.world;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.key.Key;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;

@Data
@AllArgsConstructor
public class BlockState {

    private CompoundTag tag;

    public Key getType() {
        return Key.key(tag.getString("Name"));
    }

    public void setType(Key Key) {
        tag.putString("Name", Key.toString());
    }

    public Map<String, String> getProperties() {
        Map<String, String> mapping = new HashMap<>();
        for (Map.Entry<String, Tag<?>> entry : tag.getCompoundTag("Properties")) {
            String key = entry.getKey();
            String value = ((StringTag) entry.getValue()).getValue();
            mapping.put(key, value);
        }
        return mapping;
    }

    public String getProperty(String key) {
        Tag<?> value = tag.getCompoundTag("Properties").get(key);
        return value == null ? null : ((StringTag) value).getValue();
    }

    public void setProperties(Map<String, String> mapping) {
        CompoundTag properties = new CompoundTag();
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            properties.putString(key, value);
        }
        tag.put("Properties", properties);
    }

    public <T> void setProperty(String key, T value) {
        CompoundTag properties = tag.getCompoundTag("Properties");
        properties.putString(key, ((T) value).toString());
    }


}
