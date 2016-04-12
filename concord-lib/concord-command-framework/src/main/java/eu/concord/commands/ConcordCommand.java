package eu.concord.commands;

import eu.concord.commands.exceptions.InvalidArgumentException;
import eu.concord.commands.exceptions.ParserNotFoundException;
import eu.concord.commands.impl.DefaultCommandRunner;
import eu.concord.commands.interfaces.CommandRunner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class ConcordCommand<PLUGIN extends JavaPlugin> implements CommandExecutor {

    private CommandRunner<CommandSender> defaultHandler = new DefaultCommandRunner();

    private final String name;
    private final ArgumentParseHelper parseHelper;
    private final PLUGIN plugin;

    public ConcordCommand(PLUGIN plugin, ArgumentParseHelper parseHelper, String name) {
        this.plugin = plugin;
        this.parseHelper = parseHelper;
        this.name = name;
    }

    public abstract CommandFormat[] getCommandHandlers();

    public boolean onCommand(CommandSender commandSender, Command command, String commandAlias, String[] args) {
        CommandFormat cmdF = null;
        Player p = null;
        if (commandSender instanceof Player) {
            p = (Player) commandSender;
        }
        for (int i = 0; i < this.getCommandHandlers().length; i++) {
            CommandFormat cmd = this.getCommandHandlers()[i];
            if (args.length < cmd.getRequiredArgumentLength()) {
                continue;
            }
            if (p != null && cmd.hasPermission(p)) {
                continue;
            }
            try {
                Object[] parsedArgs = this.getParseHelper().parseArguments(cmd.getArgumentTypes(), args);
                Arguments argsObj = new Arguments(parsedArgs);
                if (p != null && cmd.getPlayerHandler() != null) {
                    cmd.getPlayerHandler().run(p, argsObj);
                } else if (cmd.getConsoleHandler() != null) {
                    cmd.getConsoleHandler().run(commandSender, argsObj);
                } else {
                    this.getDefaultHandler().run(commandSender, argsObj);
                }
                return true;
            } catch (ParserNotFoundException e) {
                error(commandSender, "An error occured, please contact an administrator");
                e.printStackTrace(); // programmer error - do print
            } catch (InvalidArgumentException e) {
                continue;
            }
        }
        error(commandSender, "Invalid usage!");
        sendUsage(commandSender);
        return true;
    }

    public void sendMessage(final CommandSender p, final String... message) {
        Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                for (String m : message) {
                    p.sendMessage(m);
                }
            }
        });
    }

    public void success(CommandSender p, String msg) {
        this.sendMessage(p, ChatColor.GREEN + msg);
    }

    public void error(CommandSender p, String msg) {
        this.sendMessage(p, ChatColor.RED + msg);
    }

    public void sendUsage(CommandSender p) {
        List<String> usage = new ArrayList();
        usage.add(ChatColor.GRAY + "Usage:");
        for (CommandFormat f : getCommandHandlers()) {
            for (String s : f.getUsage()) {
                usage.add(ChatColor.BLUE + s);
            }
        }
        this.sendMessage(p, usage.toArray(new String[usage.size()]));
    }

    public String getName() {
        return this.name;
    }

    public PLUGIN getPlugin() {
        return this.plugin;
    }

    public ArgumentParseHelper getParseHelper() {
        return this.parseHelper;
    }

    public final CommandRunner<CommandSender> getDefaultHandler() {
        return this.defaultHandler;
    }

    public void setDefaultHandler(CommandRunner<CommandSender> handler) {
        if (handler == null) {
            handler = new DefaultCommandRunner();
        }
        this.defaultHandler = handler;
    }

}
