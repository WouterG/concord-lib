package eu.concord.commands.exceptions;

public class InvalidArgumentException extends Exception {

    private Class parserType;
    private String input;

    public InvalidArgumentException(Class type, String input) {
        super("The input: \"" + input + "\" cannot be parsed to " + type.getName());
        this.parserType = type;
        this.input = input;
    }

    public Class getParserType() {
        return this.parserType;
    }

    public String getInput() {
        return this.input;
    }

}
