/*
 * Copyright (C) 2020 Nan1t
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.rochblondiaux.ultralimbo.world;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.ultralimbo.Limbo;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;

@Getter
@RequiredArgsConstructor
@Log4j2
public final class DimensionRegistry {

    private final Limbo app;

    private Dimension defaultDimension_1_16;
    private Dimension defaultDimension_1_18_2;
    private Dimension dimension_1_20_5;
    private Dimension dimension_1_21;
    private Dimension dimension_1_21_4;

    private CompoundBinaryTag codec_1_16;
    private CompoundBinaryTag codec_1_18_2;
    private CompoundBinaryTag codec_1_19;
    private CompoundBinaryTag codec_1_19_1;
    private CompoundBinaryTag codec_1_19_4;
    private CompoundBinaryTag codec_1_20;
    private CompoundBinaryTag codec_1_21;
    private CompoundBinaryTag codec_1_21_4;
    private CompoundBinaryTag oldCodec;

    public void load(String def) throws IOException {
        // On 1.16-1.16.1 different codec format
        oldCodec = readCodecFile("codec_old");
        codec_1_16 = readCodecFile("codec_1_16");
        codec_1_18_2 = readCodecFile("codec_1_18_2");
        codec_1_19 = readCodecFile("codec_1_19");
        codec_1_19_1 = readCodecFile("codec_1_19_1");
        codec_1_19_4 = readCodecFile("codec_1_19_4");
        codec_1_20 = readCodecFile("codec_1_20");
        codec_1_21 = readCodecFile("codec_1_21");
        codec_1_21_4 = readCodecFile("codec_1_21_4");

        defaultDimension_1_16 = getDefaultDimension(def, codec_1_16);
        defaultDimension_1_18_2 = getDefaultDimension(def, codec_1_18_2);

        dimension_1_20_5 = getModernDimension(def, codec_1_20);
        dimension_1_21 = getModernDimension(def, codec_1_21);
        dimension_1_21_4 = getModernDimension(def, codec_1_21_4);
    }

    private Dimension getDefaultDimension(String def, CompoundBinaryTag tag) {
        ListBinaryTag dimensions = tag.getCompound("minecraft:dimension_type").getList("value");

        CompoundBinaryTag overWorld = (CompoundBinaryTag) ((CompoundBinaryTag) dimensions.get(0)).get("element");
        CompoundBinaryTag nether = (CompoundBinaryTag) ((CompoundBinaryTag) dimensions.get(2)).get("element");
        CompoundBinaryTag theEnd = (CompoundBinaryTag) ((CompoundBinaryTag) dimensions.get(3)).get("element");

        return switch (def.toLowerCase()) {
            case "overworld" -> new Dimension(0, "minecraft:overworld", overWorld);
            case "the_nether" -> new Dimension(-1, "minecraft:nether", nether);
            case "the_end" -> new Dimension(1, "minecraft:the_end", theEnd);
            default -> {
                log.warn("Undefined dimension type: '{}'. Using THE_END as default", def);
                yield new Dimension(1, "minecraft:the_end", theEnd);
            }
        };
    }

    private Dimension getModernDimension(String def, CompoundBinaryTag tag) {
        return switch (def.toLowerCase()) {
            case "overworld" -> new Dimension(0, "minecraft:overworld", tag);
            case "the_nether" -> new Dimension(2, "minecraft:nether", tag);
            case "the_end" -> new Dimension(3, "minecraft:the_end", tag);
            default -> {
                log.warn("Undefined dimension type: '{}'. Using THE_END as default", def);
                yield new Dimension(3, "minecraft:the_end", tag);
            }
        };
    }

    private CompoundBinaryTag readCodecFile(String resPath) throws IOException {
        try (InputStream inputStream = app.getClass().getResourceAsStream("/minecraft/dimensions/%s.snbt".formatted(resPath))) {
            if (inputStream == null)
                throw new FileNotFoundException("Cannot find codec file");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining("\n"));
                if (content.isBlank())
                    throw new IOException("Empty codec file");

                return TagStringIO.get().asCompound(content);
            }
        }
    }
}
