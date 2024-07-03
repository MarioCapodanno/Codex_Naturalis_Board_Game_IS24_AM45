package it.polimi.ingsw.am45.utilities;

import java.util.regex.Pattern;

public class InputConnectionValidator {


    private static final Pattern IP_ADDRESS_PATTERN =
            Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    public static boolean validate(String input) {
        return input.equals("localhost") || input.equals("rmi") || IP_ADDRESS_PATTERN.matcher(input).matches();
    }
}