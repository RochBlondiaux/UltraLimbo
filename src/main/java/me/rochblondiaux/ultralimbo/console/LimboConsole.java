package me.rochblondiaux.ultralimbo.console;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.ultralimbo.Limbo;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

@RequiredArgsConstructor
public class LimboConsole extends SimpleTerminalConsole {

    private final Limbo app;

    @Override
    protected boolean isRunning() {
        return !this.app.isStopped() && this.app.isRunning();
    }

    @Override
    protected void runCommand(String s) {
        this.app.logger().info("Command: {}", s);
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


}
