package eu.concord.commands;

import eu.concord.commands.interfaces.ArgumentVerifier;
import eu.concord.commands.interfaces.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandFormat {

    private final String[] usageLines;
    private final ConcordCommand owner;

    private String permStr;

    private Map<Integer, Class> argTypes;
    private Map<Integer, ArgumentVerifier> argVerifiers;

    private CommandRunner<CommandSender> consoleHandler;
    private CommandRunner<Player> playerHandler;

    private int arglength;

    public CommandFormat(ConcordCommand cmd, int arglength, String... usageLines) {
        this.owner = cmd;
        this.usageLines = usageLines;

        this.arglength = arglength;

        this.argTypes = new ConcurrentHashMap<Integer, Class>();
        this.argVerifiers = new ConcurrentHashMap<Integer, ArgumentVerifier>();
    }

    public boolean hasPermission(Player p) {
        return p.hasPermission(permStr);
    }

    public CommandFormat permission(String perm) {
        this.permStr = perm;
        return this;
    }

    public CommandFormat types(Class... types) {
        for (int i = 0; i < types.length; i++) {
            argTypes.put(i, types[i]);
        }
        return this;
    }

    public CommandFormat verify(int argIndex, ArgumentVerifier verifier) {
        if (argIndex < 0 || argIndex > 1024) {
            return this;
        }
        this.argVerifiers.put(argIndex, verifier);
        return this;
    }

    public String[] getUsage() {
        return this.usageLines;
    }

    protected Map<Integer, Class> getArgumentTypes() {
        return this.argTypes;
    }

    protected Map<Integer, ArgumentVerifier> getArgumentVerifiers() {
        return this.argVerifiers;
    }

    public int getRequiredArgumentLength() {
        return this.arglength;
    }

    public CommandFormat console(CommandRunner<CommandSender> runner) {
        this.consoleHandler = runner;
        return this;
    }

    public CommandFormat player(CommandRunner<Player> runner) {
        this.playerHandler = runner;
        return this;
    }

    public CommandRunner<CommandSender> getConsoleHandler() {
        return this.consoleHandler;
    }

    public CommandRunner<Player> getPlayerHandler() {
        return this.playerHandler;
    }

}
