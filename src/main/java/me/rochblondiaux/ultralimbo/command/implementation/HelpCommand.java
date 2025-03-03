package me.rochblondiaux.ultralimbo.command.implementation;

import java.util.List;

import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.command.BaseCommand;
import me.rochblondiaux.ultralimbo.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class HelpCommand extends BaseCommand {

    private final Limbo app;

    public HelpCommand(Limbo app) {
        super("help", List.of("?", "h"), "Display the help message", "/help");
        this.app = app;
    }

    @Override
    public void execute(CommandSender executor, String command, String[] args) {
        executor.sendMessage(Component.text("Available commands:", NamedTextColor.AQUA));
        this.app.commands()
                .commands()
                .forEach((name, cmd) -> executor.sendMessage(Component.text(cmd.syntax(), NamedTextColor.AQUA)
                        .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                        .append(Component.text(cmd.description(), NamedTextColor.AQUA))));
    }
}
