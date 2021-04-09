package com.javatasks.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.javatasks.util.Constants.EQUALS_OPERATOR;
import static com.javatasks.util.Constants.UNARY_MINUS_OPERATOR;

public class CommandOptionsService {

    public static String valueOf(String[] arguments, String option)
    {
        String value = null;
        String str;

        for ( int i = 0; i < arguments.length; i++ ) {
            str = arguments[i];
            if (str.equalsIgnoreCase(option) && arguments[i + 1].equals(EQUALS_OPERATOR)) {
                value = arguments[i + 2].replace("'", "");
                break;
            }
        }
        return value;
    }

    public static boolean checkIsOption(String argument) {
        return argument.startsWith(UNARY_MINUS_OPERATOR);
    }

    public static void checkArgumentsLength(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No arguments are provided!");
        }
    }

    public static List<String> getAllParameters(String[] args) {
        List<String> parameters = new ArrayList<>();

        for (int i = 1; i < args.length; i++) {
            if (checkIsOption(args[i])) {
                parameters.add(args[i]);
            }
        }
        return parameters;
    }

    public static boolean checkMethodsParameters(List<String> actualValues, List<String> expectedValues) {
        return new HashSet<>(expectedValues).equals(new HashSet<>(actualValues));
    }

}
