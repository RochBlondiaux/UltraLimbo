package me.rochblondiaux.ultralimbo.command;

import me.rochblondiaux.ultralimbo.model.Nameable;
import net.kyori.adventure.audience.Audience;

public interface CommandSender extends Nameable, Audience {

    @Deprecated
    void sendMessage(String message);
}
