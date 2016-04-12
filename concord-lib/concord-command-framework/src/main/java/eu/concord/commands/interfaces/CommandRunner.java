package eu.concord.commands.interfaces;

import eu.concord.commands.Arguments;
import org.bukkit.command.CommandSender;

public interface CommandRunner<T extends CommandSender> {

    void run(T sender, Arguments args);

}
