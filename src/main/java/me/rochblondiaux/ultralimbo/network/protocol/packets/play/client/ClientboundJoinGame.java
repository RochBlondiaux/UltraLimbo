package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import org.jetbrains.annotations.Nullable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import me.rochblondiaux.ultralimbo.player.GameMode;
import me.rochblondiaux.ultralimbo.world.DimensionRegistry;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundJoinGame implements ClientboundPacket {

    private int id;
    private boolean hardcore;
    private GameMode gameMode;
    private @Nullable GameMode previousGameMode;
    private String[] worldNames;
    private DimensionRegistry dimensionRegistry;
    private String worldName;
    private long hashedSeed;
    private int maxPlayers;
    private int viewDistance;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;
    private boolean debug;
    private boolean flat;
    private boolean limitedCrafting;
    private boolean secureProfile = true;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeInt(this.id);

        if (version.fromTo(Version.V1_7_2, Version.V1_7_6)) {
            msg.writeByte(gameMode.ordinal() == 3 ? 1 : gameMode.ordinal());
            msg.writeByte(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(0); // Difficulty
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
        }

        if (version.fromTo(Version.V1_8, Version.V1_9)) {
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(0); // Difficulty
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeBoolean(reducedDebugInfo);
        }

        if (version.fromTo(Version.V1_9_1, Version.V1_13_2)) {
            msg.writeByte(gameMode.ordinal());
            msg.writeInt(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(0); // Difficulty
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeBoolean(reducedDebugInfo);
        }

        if (version.fromTo(Version.V1_14, Version.V1_14_4)) {
            msg.writeByte(gameMode.ordinal());
            msg.writeInt(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
        }

        if (version.fromTo(Version.V1_15, Version.V1_15_2)) {
            msg.writeByte(gameMode.ordinal());
            msg.writeInt(dimensionRegistry.defaultDimension_1_16().id());
            msg.writeLong(hashedSeed);
            msg.writeByte(maxPlayers);
            msg.writeString("flat"); // Level type
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
        }

        if (version.fromTo(Version.V1_16, Version.V1_16_1)) {
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeStringsArray(worldNames);
            msg.writeCompoundTag(dimensionRegistry.oldCodec());
            msg.writeString(dimensionRegistry.defaultDimension_1_16().name());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
        }

        if (version.fromTo(Version.V1_16_2, Version.V1_17_1)) {
            msg.writeBoolean(hardcore);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeStringsArray(worldNames);
            msg.writeCompoundTag(dimensionRegistry.codec_1_16());
            msg.writeCompoundTag(dimensionRegistry.defaultDimension_1_16().data());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
        }

        if (version.fromTo(Version.V1_18, Version.V1_18_2)) {
            msg.writeBoolean(hardcore);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeStringsArray(worldNames);
            if (version.moreOrEqual(Version.V1_18_2)) {
                msg.writeCompoundTag(dimensionRegistry.codec_1_18_2());
                msg.writeCompoundTag(dimensionRegistry.defaultDimension_1_18_2().data());
            } else {
                msg.writeCompoundTag(dimensionRegistry.codec_1_16());
                msg.writeCompoundTag(dimensionRegistry.defaultDimension_1_16().data());
            }
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
        }

        if (version.fromTo(Version.V1_19, Version.V1_19_4)) {
            msg.writeBoolean(hardcore);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeStringsArray(worldNames);
            if (version.moreOrEqual(Version.V1_19_1)) {
                if (version.moreOrEqual(Version.V1_19_4)) {
                    msg.writeCompoundTag(dimensionRegistry.codec_1_19_4());
                }
                else {
                    msg.writeCompoundTag(dimensionRegistry.codec_1_19_1());
                }
            }
            else {
                msg.writeCompoundTag(dimensionRegistry.codec_1_19());
            }
            msg.writeString(worldName); // World type
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
            msg.writeBoolean(false);
        }

        if (version.equals(Version.V1_20)) {
            msg.writeBoolean(hardcore);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeStringsArray(worldNames);
            msg.writeCompoundTag(dimensionRegistry.codec_1_20());
            msg.writeString(worldName); // World type
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
        }

        if (version.fromTo(Version.V1_20_2, Version.V1_20_3)) {
            msg.writeBoolean(hardcore);
            msg.writeStringsArray(worldNames);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(limitedCrafting);
            msg.writeString(worldName);
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
        }

        if (version.fromTo(Version.V1_20_5, Version.V1_21)) {
            msg.writeBoolean(hardcore);
            msg.writeStringsArray(worldNames);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(limitedCrafting);
            msg.writeVarInt(dimensionRegistry.dimension_1_20_5().id());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
            msg.writeBoolean(secureProfile);
        }

        if (version.equals(Version.V1_21_2)) {
            msg.writeBoolean(hardcore);
            msg.writeStringsArray(worldNames);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(limitedCrafting);
            msg.writeVarInt(dimensionRegistry.dimension_1_21().id());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
            msg.writeVarInt(0);
            msg.writeBoolean(secureProfile);
        }

        if (version.moreOrEqual(Version.V1_21_4)) {
            msg.writeBoolean(hardcore);
            msg.writeStringsArray(worldNames);
            msg.writeVarInt(maxPlayers);
            msg.writeVarInt(viewDistance);
            msg.writeVarInt(viewDistance); // Simulation Distance
            msg.writeBoolean(reducedDebugInfo);
            msg.writeBoolean(enableRespawnScreen);
            msg.writeBoolean(limitedCrafting);
            msg.writeVarInt(dimensionRegistry.dimension_1_21_4().id());
            msg.writeString(worldName);
            msg.writeLong(hashedSeed);
            msg.writeByte(gameMode.ordinal());
            msg.writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            msg.writeBoolean(debug);
            msg.writeBoolean(flat);
            msg.writeBoolean(false);
            msg.writeVarInt(0);
            msg.writeVarInt(0);
            msg.writeBoolean(secureProfile);
        }
    }
}
