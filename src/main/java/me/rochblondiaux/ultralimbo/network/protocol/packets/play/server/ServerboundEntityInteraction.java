package me.rochblondiaux.ultralimbo.network.protocol.packets.play.server;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.rochblondiaux.ultralimbo.network.protocol.ByteMessage;
import me.rochblondiaux.ultralimbo.network.protocol.ServerboundPacket;
import me.rochblondiaux.ultralimbo.network.protocol.registry.Version;
import me.rochblondiaux.ultralimbo.utils.Vector;

@NoArgsConstructor
@AllArgsConstructor
public class ServerboundEntityInteraction implements ServerboundPacket {

    private int id;
    private InteractAction action;
    private Optional<Vector> target;
    private InteractionHand hand;
    private boolean sneaking;

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.id = msg.readVarInt();

        int typeIndex = msg.readVarInt();
        this.action = InteractAction.VALUES[typeIndex];
        if (action == InteractAction.INTERACT_AT) {
            float x = msg.readFloat();
            float y = msg.readFloat();
            float z = msg.readFloat();
            this.target = Optional.of(new Vector(x, y, z));
        } else
            this.target = Optional.empty();

        if (action == InteractAction.INTERACT || action == InteractAction.INTERACT_AT) {
            int handID = msg.readVarInt();
            this.hand = InteractionHand.values()[handID];
        } else
            this.hand = InteractionHand.MAIN_HAND;

        this.sneaking = msg.readBoolean();
    }

    public enum InteractAction {
        INTERACT, ATTACK, INTERACT_AT;
        public static final InteractAction[] VALUES = values();
    }

    public enum InteractionHand {
        MAIN_HAND, OFF_HAND;
        private static final InteractionHand[] VALUES = values();
    }
}
