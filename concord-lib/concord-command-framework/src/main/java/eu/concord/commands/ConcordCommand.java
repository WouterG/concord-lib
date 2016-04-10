package eu.concord.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class ConcordCommand implements CommandExecutor {

    private final String name;

    public ConcordCommand(String name) {
        this.name = name;
    }

    public abstract CommandFormat[] getCommandHandlers();

    public boolean onCommand(CommandSender commandSender, Command command, String commandAlias, String[] args) {

        return true;
    }
}
