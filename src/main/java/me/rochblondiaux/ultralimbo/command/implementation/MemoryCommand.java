package me.rochblondiaux.ultralimbo.command.implementation;

import java.util.List;

import me.rochblondiaux.ultralimbo.command.BaseCommand;
import me.rochblondiaux.ultralimbo.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MemoryCommand extends BaseCommand {

    public MemoryCommand() {
        super("memory", List.of("mem"), "Show memory usage.", "/memory");
    }

    @Override
    public void execute(CommandSender executor, String command, String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long mb = 1024 * 1024;
        long used = (runtime.totalMemory() - runtime.freeMemory()) / mb;
        long total = runtime.totalMemory() / mb;
        long free = runtime.freeMemory() / mb;
        long max = runtime.maxMemory() / mb;

        executor.sendMessage(Component.text("        ", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH)
                .appendSpace()
                .append(Component.text("Memory usage", NamedTextColor.AQUA).decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.FALSE))
                .appendSpace()
                .append(Component.text("        ", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH)));
        executor.sendMessage(Component.text("Used: ", NamedTextColor.GRAY)
                .append(Component.text(used, NamedTextColor.AQUA))
                .append(Component.text(" MB", NamedTextColor.GRAY)));
        executor.sendMessage(Component.text("Total: ", NamedTextColor.GRAY)
                .append(Component.text(total, NamedTextColor.AQUA))
                .append(Component.text(" MB", NamedTextColor.GRAY)));
        executor.sendMessage(Component.text("Free: ", NamedTextColor.GRAY)
                .append(Component.text(free, NamedTextColor.AQUA))
                .append(Component.text(" MB", NamedTextColor.GRAY)));
        executor.sendMessage(Component.text("Max: ", NamedTextColor.GRAY)
                .append(Component.text(max, NamedTextColor.AQUA))
                .append(Component.text(" MB", NamedTextColor.GRAY)));
        executor.sendMessage(Component.empty());
    }
}
