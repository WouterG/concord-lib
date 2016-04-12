package eu.concord.commands;

public class Arguments {

    private Object[] args;

    public Arguments(Object[] args) {
        this.args = args;
    }

    public <T> T get(int index, Class<T> type) {
        try {
            return type.cast(args[index]);
        } catch (Exception ex) {
            return null;
        }
    }

}
