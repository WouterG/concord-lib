package eu.concord.commands;

import eu.concord.commands.interfaces.ArgumentParser;
import eu.concord.commands.interfaces.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFormat {

    private static CommandRunner<CommandSender> defaultRunner;

    private final String usage;
    private final ArgumentParser[] args;

    private CommandRunner<CommandSender> consoleHandler;
    private CommandRunner<Player> playerHandler;

    public CommandFormat(String usage, ArgumentParser... args) {
        this.usage = usage;
        this.args = args;
    }

    public void console(CommandRunner<CommandSender> runner) {

    }

}
