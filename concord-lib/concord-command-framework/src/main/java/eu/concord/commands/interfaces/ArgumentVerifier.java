package eu.concord.commands.interfaces;

public interface ArgumentVerifier<T> {

    boolean verify(T object);

}
