package eu.concord.commands.interfaces;

public interface ArgumentParser<T> {

    T parseArgument(String input) throws Exception;

}
