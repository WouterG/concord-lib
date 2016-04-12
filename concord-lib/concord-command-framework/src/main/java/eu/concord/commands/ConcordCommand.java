package eu.concord.commands;

import eu.concord.commands.impl.DefaultArgumentVerifier;
import eu.concord.commands.impl.DefaultCommandRunner;
import eu.concord.commands.interfaces.ArgumentVerifier;
import eu.concord.commands.interfaces.CommandRunner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ConcordCommand<PLUGIN extends JavaPlugin> implements CommandExecutor {


    private CommandRunner<CommandSender> defaultHandler = new DefaultCommandRunner();
    private ArgumentVerifier defaultVerifier = new DefaultArgumentVerifier();

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
        for (int i = 0; i < this.getCommandHandlers().length; i++) {
            CommandFormat cmd = this.getCommandHandlers()[i];
            if (args.length < cmd.getRequiredArgumentLength()) {
                continue;
            }
        }

        return true;
    }

    public void sendMessage(final Player p, final String... message) {
        Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                for (String m : message) {
                    p.sendMessage(m);
                }
            }
        });
    }

    public void success(Player p, String msg) {
        this.sendMessage(p, ChatColor.GREEN + msg);
    }

    public void error(Player p, String msg) {
        this.sendMessage(p, ChatColor.RED + msg);
    }

    public void sendUsage(Player p) {
        // TODO
        this.sendMessage(p, "");
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

    public final ArgumentVerifier getDefaultVerifier() {
        return this.defaultVerifier;
    }

    public void setDefaultHandler(CommandRunner<CommandSender> handler) {
        if (handler == null) {
            handler = new DefaultCommandRunner();
        }
        this.defaultHandler = handler;
    }

    public void setDefaultVerifier(ArgumentVerifier verifier) {
        if (verifier == null) {
            verifier = new DefaultArgumentVerifier();
        }
        this.defaultVerifier = verifier;
    }

}
