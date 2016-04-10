package eu.concord.commands;

import eu.concord.commands.interfaces.ArgumentParser;

public class PrimitiveArgs {

    public static class IntegerArgument implements ArgumentParser<Integer> {
        public Integer parseArgument(String input) throws Exception {
            return Integer.parseInt(input);
        }
    }

    public static class LongArgument implements ArgumentParser<Long> {
        public Long parseArgument(String input) throws Exception {
            return Long.parseLong(input);
        }
    }

    public static class FloatArgument implements ArgumentParser<Float> {
        public Float parseArgument(String input) throws Exception {
            return Float.parseFloat(input);
        }
    }

    public static class DoubleArgument implements ArgumentParser<Double> {
        public Double parseArgument(String input) {
            return Double.parseDouble(input);
        }
    }

    public static class BooleanArgument implements ArgumentParser<Boolean> {
        public Boolean parseArgument(String input) throws Exception {
            if (input.equalsIgnoreCase("true")) {
                return true;
            } else if (input.equalsIgnoreCase("false")) {
                return false;
            }
            throw new Exception();
        }
    }

    public static class StringArgument implements ArgumentParser<String> {
        public String parseArgument(String input) throws Exception {
            return input;
        }
    }

}
