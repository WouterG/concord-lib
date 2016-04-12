package eu.concord.commands;

import eu.concord.commands.exceptions.InvalidArgumentException;
import eu.concord.commands.exceptions.ParserNotFoundException;
import eu.concord.commands.interfaces.ArgumentParser;
import eu.concord.commands.utils.CommandUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArgumentParseHelper {

    private Map<Class, ArgumentParser> parsers = new ConcurrentHashMap<Class, ArgumentParser>();
    private boolean combineQuotedArguments;

    public ArgumentParseHelper() {
        this(true);
    }

    public ArgumentParseHelper(boolean combineQuotedArguments) {
        this.combineQuotedArguments = combineQuotedArguments;
        parsers = new ConcurrentHashMap();
        parsers.put(Integer.class, new IntegerArgument());
        parsers.put(Long.class, new LongArgument());
        parsers.put(Float.class, new FloatArgument());
        parsers.put(Double.class, new DoubleArgument());
        parsers.put(Boolean.class, new BooleanArgument());
        parsers.put(String.class, new StringArgument());
    }

    public Object parseArguments(Class[] types, String[] args) throws ParserNotFoundException, InvalidArgumentException {
        if (this.combineQuotedArguments) {
            args = CommandUtils.combineQuotedArgs(args);
        }
        if (args.length != types.length) {
            return false;
        }
        List<Object> parsedArguments = new ArrayList();
        for (int i = 0; i < types.length; i++) {
            ArgumentParser parser = getParser(types[i]);
            if (parser == null) {
                throw new ParserNotFoundException(types[i]);
            }
            try {
                Object parsed = parser.parseArgument(args[i]);
                parsedArguments.add(parsed);
            } catch (Exception e) {
                throw new InvalidArgumentException(types[i], args[i]);
            }
        }
        return parsedArguments;
    }

    public <T> void registerParser(Class<T> type, ArgumentParser<T> parser) {
        parsers.put(type, parser);
    }

    public void unregisterParser(Class type) {
        parsers.remove(type);
    }

    public <T> ArgumentParser<T> getParser(Class<T> type) {
        if (!parsers.containsKey(type)) {
            return null;
        }
        return (ArgumentParser<T>) parsers.get(type);
    }

    private static class IntegerArgument implements ArgumentParser<Integer> {
        public Integer parseArgument(String input) throws Exception {
            return Integer.parseInt(input);
        }
    }

    private static class LongArgument implements ArgumentParser<Long> {
        public Long parseArgument(String input) throws Exception {
            return Long.parseLong(input);
        }
    }

    private static class FloatArgument implements ArgumentParser<Float> {
        public Float parseArgument(String input) throws Exception {
            try {
                return Float.parseFloat(input);
            } catch (Exception e) {
                return (float) Integer.parseInt(input);
            }
        }
    }

    private static class DoubleArgument implements ArgumentParser<Double> {
        public Double parseArgument(String input) {
            try {
                return Double.parseDouble(input);
            } catch (Exception e) {
                return (double) Integer.parseInt(input);
            }
        }
    }

    private static class BooleanArgument implements ArgumentParser<Boolean> {
        public Boolean parseArgument(String input) throws Exception {
            if (input.equalsIgnoreCase("true")) {
                return true;
            } else if (input.equalsIgnoreCase("false")) {
                return false;
            }
            throw new Exception();
        }
    }

    private static class StringArgument implements ArgumentParser<String> {
        public String parseArgument(String input) throws Exception {
            return input;
        }
    }

}
