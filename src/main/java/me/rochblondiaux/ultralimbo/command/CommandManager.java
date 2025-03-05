package me.rochblondiaux.ultralimbo.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Unmodifiable;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.ultralimbo.Limbo;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

@RequiredArgsConstructor
public class CommandManager {

    private final Limbo app;
    private final Map<String, BaseCommand> commands = new HashMap<>();

    public void register(BaseCommand command) {
        this.commands.put(command.name(), command);
    }

    public void unregister(String name) {
        this.commands.remove(name);
    }

    public Optional<BaseCommand> findByName(String name) {
        return Optional.ofNullable(this.commands.get(name));
    }

    public Optional<BaseCommand> findByNameOrAlias(String name) {
        return this.commands.values()
                .stream()
                .filter(command -> command.name().equalsIgnoreCase(name) || command.aliases().contains(name))
                .findFirst();
    }

    @Unmodifiable
    public Map<String, BaseCommand> commands() {
        return Map.copyOf(this.commands);
    }

    public void handleExecution(CommandSender executor, String raw) {
        String[] args = raw.split(" ");
        String commandName = args[0].toLowerCase();

        String[] arguments = new String[args.length - 1];
        System.arraycopy(args, 1, arguments, 0, arguments.length);

        this.findByNameOrAlias(commandName)
                .ifPresentOrElse(command -> command.execute(executor, commandName, arguments), () -> this.app.configuration().message(executor, "unknown-command", Placeholder.parsed("command", raw)));
    }

}
