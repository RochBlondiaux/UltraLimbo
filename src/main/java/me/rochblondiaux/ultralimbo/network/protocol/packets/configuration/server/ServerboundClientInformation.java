package me.rochblondiaux.ultralimbo.network.protocol.packets.configuration.server;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ServerboundClientInformation implements ServerboundPacket {

    private Locale locale;
    private int viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private SkinPart[] skinParts;
    private int mainHand;
    private boolean enableTextFiltering;
    private boolean allowServerListing;
    private ParticleStatus particleStatus;

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.locale = Locale.forLanguageTag(msg.readString(16));
        this.viewDistance = msg.readByte();
        this.chatMode = ChatMode.values()[msg.readByte()];
        this.chatColors = msg.readBoolean();
        this.skinParts = SkinPart.fromBitMask(msg.readByte());
        this.mainHand = msg.readByte();
        this.enableTextFiltering = msg.readBoolean();
        this.allowServerListing = msg.readBoolean();
        this.particleStatus = ParticleStatus.values()[msg.readByte()];
    }

    public enum ChatMode {
        ENABLED,
        COMMANDS_ONLY,
        HIDDEN
    }

    public enum ParticleStatus {
        ENABLED,
        DECREASED,
        DISABLED
    }

    @Getter
    @RequiredArgsConstructor
    public enum SkinPart {
        CAPE(0x01),
        JACKET(0x02),
        LEFT_SLEEVE(0x04),
        RIGHT_SLEEVE(0x08),
        LEFT_PANTS_LEG(0x10),
        RIGHT_PANTS_LEG(0x20),
        HAT(0x40);

        private final int mask;

        public static SkinPart[] fromBitMask(byte mask) {
            SkinPart[] parts = new SkinPart[7];
            int index = 0;
            for (SkinPart part : values()) {
                if ((mask & part.mask) == part.mask) {
                    parts[index++] = part;
                }
            }
            System.arraycopy(parts, 0, parts, 0, index);
            return parts;
        }
    }
}
