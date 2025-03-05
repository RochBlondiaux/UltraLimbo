package me.rochblondiaux.ultralimbo.network.protocol.packets.play.client;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ClientboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import net.kyori.adventure.bossbar.BossBar;

@NoArgsConstructor
@AllArgsConstructor
public class ClientboundBossBar implements ClientboundPacket {

    private UUID uniqueId;
    private Action action;
    private BossBar bossBar;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeUuid(this.uniqueId);
        msg.writeVarInt(action.ordinal());

        switch (action) {
            case ADD -> {
                msg.writeComponent(bossBar.name());
                msg.writeFloat(bossBar.progress());
                msg.writeVarInt(bossBar.color().ordinal());
                msg.writeVarInt(bossBar.overlay().ordinal());
                msg.writeByte(bitMask(bossBar));
            }
            case REMOVE -> {}
            case UPDATE_PROGRESS -> msg.writeFloat(bossBar.progress());
            case UPDATE_NAME -> msg.writeComponent(bossBar.name());
            case UPDATE_STYLE -> {
                msg.writeVarInt(bossBar.color().ordinal());
                msg.writeVarInt(bossBar.overlay().ordinal());
            }
            case UPDATE_PROPERTIES -> msg.writeByte(bitMask(bossBar));
        }
    }

    private int bitMask(BossBar bossBar) {
        int bitMask = 0;
        if (bossBar.hasFlag(BossBar.Flag.DARKEN_SCREEN))
            bitMask |= 1;
        if (bossBar.hasFlag(BossBar.Flag.PLAY_BOSS_MUSIC))
            bitMask |= 2;
        if (bossBar.hasFlag(BossBar.Flag.CREATE_WORLD_FOG))
            bitMask |= 4;
        return bitMask;
    }

    public enum Action {
        ADD,
        REMOVE,
        UPDATE_PROGRESS,
        UPDATE_NAME,
        UPDATE_STYLE,
        UPDATE_PROPERTIES;
    }
}
