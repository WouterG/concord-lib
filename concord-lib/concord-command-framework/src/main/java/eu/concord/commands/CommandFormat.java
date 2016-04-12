package eu.concord.commands;

import eu.concord.commands.interfaces.ArgumentVerifier;
import eu.concord.commands.interfaces.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandFormat {

    private final String usage;
    private final ConcordCommand owner;

    private String permStr;

    private Map<Integer, Class> argTypes;
    private Map<Integer, ArgumentVerifier> argVerifiers;

    private CommandRunner<CommandSender> consoleHandler;
    private CommandRunner<Player> playerHandler;

    private int arglength;

    public CommandFormat(ConcordCommand cmd, String usage, int arglength) {
        this.owner = cmd;
        this.usage = usage;

        this.arglength = arglength;

        this.argTypes = new ConcurrentHashMap<Integer, Class>();
        this.argVerifiers = new ConcurrentHashMap<Integer, ArgumentVerifier>();
    }

    public boolean hasPermission(Player p) {
        return p.hasPermission(permStr);
    }

    public void permission(String perm) {
        this.permStr = perm;
    }

    public void types(Class... types) {
        for (int i = 0; i < types.length; i++) {
            argTypes.put(i, types[i]);
        }
    }

    public void verify(int argIndex, ArgumentVerifier verifier) {
        if (argIndex < 0 || argIndex > 1024) {
            return;
        }
        this.argVerifiers.put(argIndex, verifier);
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

    public void console(CommandRunner<CommandSender> runner) {
        this.consoleHandler = runner;
    }

    public void player(CommandRunner<Player> runner) {
        this.playerHandler = runner;
    }

    public CommandRunner<CommandSender> getConsoleHandler() {
        return this.consoleHandler;
    }

    public CommandRunner<Player> getPlayerHandler() {
        return this.playerHandler;
    }

}
