package eu.concord.commands.interfaces;

import org.bukkit.command.CommandSender;

public interface CommandRunner<T extends CommandSender> {

    public void run(T sender, String[] args);

}
