package eu.concord.commands.exceptions;

public class ParserNotFoundException extends Exception {

    private Class c;

    public ParserNotFoundException(Class c) {
        super("Parser not found for type: " + c.getName());
        this.c = c;
    }

    public Class getType() {
        return this.c;
    }

}
