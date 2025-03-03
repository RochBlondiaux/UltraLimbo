package me.rochblondiaux.ultralimbo.console;

import org.jetbrains.annotations.NotNull;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.ultralimbo.Limbo;
import me.rochblondiaux.ultralimbo.command.CommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.ansi.ColorLevel;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

@RequiredArgsConstructor
public class LimboConsole extends SimpleTerminalConsole implements CommandSender {

    private final Limbo app;
    private final ANSIComponentSerializer serializer = ANSIComponentSerializer.builder()
            .build();

    @Override
    protected boolean isRunning() {
        return !this.app.isStopped() && this.app.isRunning();
    }

    @Override
    protected void runCommand(String s) {
        if (s == null || s.isBlank())
            return;

        this.app.commands().handleExecution(this, s);
    }

    @Override
    protected void shutdown() {
        this.app.halt();
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        builder.appName("UltraLimbo")
                .variable(LineReader.HISTORY_FILE, java.nio.file.Paths.get(".console_history"))
                .option(LineReader.Option.COMPLETE_IN_WORD, true);

        // TODO: parsing - completion - highlighting
        return super.buildReader(builder);
    }


    @Override
    public void sendMessage(String message) {
        this.app.logger().info(message);
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        this.app.logger().info(this.serializer.serialize(message));
    }

    @Override
    public String name() {
        return "console";
    }
}
