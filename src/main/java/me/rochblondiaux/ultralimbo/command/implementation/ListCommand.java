package me.rochblondiaux.ultralimbo.command.implementation;

import java.util.List;

import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.command.BaseCommand;
import me.rochblondiaux.ultralimbo.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ListCommand extends BaseCommand {

    private final Limbo app;

    public ListCommand(Limbo app) {
        super("list", List.of(), "List online players.", "/list [all]");
        this.app = app;
    }

    @Override
    public void execute(CommandSender executor, String command, String[] args) {
        executor.sendMessage(Component.text("There are ", NamedTextColor.GRAY)
                .append(Component.text(app.playerManager().count(), NamedTextColor.AQUA))
                .append(Component.text(" players online.", NamedTextColor.GRAY)));

        if (args.length > 0 && args[0].equalsIgnoreCase("all"))
            app.playerManager().players().forEach(player -> executor.sendMessage(Component.space()
                    .append(Component.text("-", NamedTextColor.GRAY))
                    .append(Component.space())
                    .append(Component.text(player.name(), NamedTextColor.AQUA))));
    }
}
