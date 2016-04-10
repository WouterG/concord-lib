package eu.concord.commands.interfaces;

public interface ArgumentType<T> {

    T parseArgument(String input) throws Exception;

}
