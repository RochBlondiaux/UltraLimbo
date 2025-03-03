package me.rochblondiaux.ultralimbo.command;

import java.util.List;

import lombok.Data;

@Data
public abstract class BaseCommand {

    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String syntax;

    public abstract void execute(CommandSender executor, String command, String[] args);

}
