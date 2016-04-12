package eu.concord.commands.impl;

import eu.concord.commands.Arguments;
import eu.concord.commands.interfaces.CommandRunner;
import org.bukkit.command.CommandSender;

public class DefaultCommandRunner implements CommandRunner<CommandSender> {

    public void run(CommandSender sender, Arguments args) {
        sender.sendMessage("This command cannot be ran.");
    }

}
