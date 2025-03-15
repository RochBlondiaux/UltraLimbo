package me.rochblondiaux.ultralimbo.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.experimental.UtilityClass;
import me.rochblondiaux.ultralimbo.world.Environment;
import me.rochblondiaux.ultralimbo.world.World;
import net.kyori.adventure.key.Key;
import net.querz.mca.Chunk;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

@UtilityClass
public class SchematicUtils {

    public static CompoundTag toTileEntityTag(CompoundTag tag) {
        int[] pos = tag.getIntArray("Pos");
        tag.remove("Pos");
        tag.remove("Id");
        tag.putInt("x", pos[0]);
        tag.putInt("y", pos[1]);
        tag.putInt("z", pos[2]);
        return tag;
    }

    public static CompoundTag toBlockTag(String input) {
        int index = input.indexOf("[");
        CompoundTag tag = new CompoundTag();
        if (index < 0) {
            tag.putString("Name", Key.key(input).toString());
            return tag;
        }

        tag.putString("Name", Key.key(input.substring(0, index)).toString());

        String[] states = input.substring(index + 1, input.lastIndexOf("]")).replace(" ", "").split(",");

        CompoundTag properties = new CompoundTag();
        for (String state : states) {
            String key = state.substring(0, state.indexOf("="));
            String value = state.substring(state.indexOf("=") + 1);
            properties.putString(key, value);
        }

        tag.put("Properties", properties);

        return tag;
    }

    public static World parseWorld(String name, Environment environment, CompoundTag nbt) {
        short width = nbt.getShort("Width");
        short length = nbt.getShort("Length");
        //short height = nbt.getShort("Height");
        byte[] blockdata = nbt.getByteArray("BlockData");
        CompoundTag palette = nbt.getCompoundTag("Palette");
        ListTag<CompoundTag> blockEntities = nbt.containsKey("BlockEntities") ? nbt.getListTag("BlockEntities").asTypedList(CompoundTag.class) : null;
        Map<Integer, String> mapping = new HashMap<>();
        if (palette != null) {
            for (String key : palette.keySet()) {
                mapping.put(palette.getInt(key), key);
            }
        }

        World world = new World(name, width, length, environment);

        int index = 0;
        int i = 0;
        int value;
        int varIntLength;
        while (i < blockdata.length) {
            value = 0;
            varIntLength = 0;

            while (true) {
                value |= (blockdata[i] & 127) << (varIntLength++ * 7);
                if (varIntLength > 5) {
                    throw new RuntimeException("VarInt too big (probably corrupted data)");
                }
                if ((blockdata[i] & 128) != 128) {
                    i++;
                    break;
                }
                i++;
            }
            // index = (y * length + z) * width + x
            int y = index / (width * length);
            int z = (index % (width * length)) / width;
            int x = (index % (width * length)) % width;
            world.setBlock(x, y, z, mapping.get(value));

            Chunk chunk = world.chunkAtWorldPos(x, z);

            if (blockEntities != null) {
                Iterator<CompoundTag> itr = blockEntities.iterator();
                while (itr.hasNext()) {
                    CompoundTag tag = itr.next();
                    int[] pos = tag.getIntArray("Pos");

                    if (pos[0] == x && pos[1] == y && pos[2] == z) {
                        ListTag<CompoundTag> newTag = chunk.getTileEntities();
                        newTag.add(toTileEntityTag(tag));
                        chunk.setTileEntities(newTag);
                        itr.remove();
                        break;
                    }
                }
            }

            index++;
        }

        for (Chunk[] chunkarray : world.chunks()) {
            for (Chunk chunk : chunkarray) {
                if (chunk != null) {
                    CompoundTag heightMap = new CompoundTag();
                    heightMap.putLongArray("MOTION_BLOCKING", new long[]{1371773531765642314L, 1389823183635651148L, 1371738278539598925L, 1389823183635388492L, 1353688558756731469L, 1389823114781694027L, 1317765589597723213L, 1371773531899860042L, 1389823183635651149L, 1371773462911685197L, 1389823183635650636L, 1353688626805119565L, 1371773531900123211L, 1335639250618849869L, 1371738278674077258L, 1389823114781694028L, 1353723811310638154L, 1371738278674077259L, 1335674228429068364L, 1335674228429067338L, 1335674228698027594L, 1317624576693539402L, 1335709481520370249L, 1299610178184057417L, 1335638906349064264L, 1299574993811968586L, 1299574924958011464L, 1299610178184056904L, 1299574924958011464L, 1299610109330100296L, 1299574924958011464L, 1299574924823793736L, 1299574924958011465L, 1281525273222484040L, 1299574924958011464L, 1281525273222484040L, 9548107335L});
                    chunk.setHeightMaps(heightMap);
                    chunk.setBiomes(new int[1024]);
                    chunk.cleanupPalettesAndBlockStates();
                }
            }
        }

        return world;
    }
}
