package me.rochblondiaux.ultralimbo.command.implementation;

import java.util.List;

import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.command.BaseCommand;
import me.rochblondiaux.ultralimbo.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class StopCommand extends BaseCommand {

    private final Limbo app;

    public StopCommand(Limbo app) {
        super("stop", List.of("shutdown"), "Stop the server.", "/stop");
        this.app = app;
    }

    @Override
    public void execute(CommandSender executor, String command, String[] args) {
        this.app.configuration().message(executor, "stopping-server");
        app.halt();
    }
}
