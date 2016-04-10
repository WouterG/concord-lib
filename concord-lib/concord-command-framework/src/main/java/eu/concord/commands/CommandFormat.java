package eu.concord.commands;

import eu.concord.commands.interfaces.ArgumentType;
import eu.concord.commands.interfaces.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFormat {

    private static CommandRunner<CommandSender> defaultRunner;

    private final String usage;
    private final ArgumentType[] args;

    private CommandRunner<CommandSender> consoleHandler;
    private CommandRunner<Player> playerHandler;

    public CommandFormat(String usage, ArgumentType... args) {
        this.usage = usage;
        this.args = args;
    }

    public void console(CommandRunner<CommandSender> runner) {

    }

}
