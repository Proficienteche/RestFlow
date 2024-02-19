package com.proficient.restapi.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateObjects {
    public static <T> void mandatory(T object) {
        Objects.requireNonNull(object);
        if (object instanceof String && ((String) object).isEmpty())
            throw new IllegalArgumentException();
    }

    public static <T> void mandatory(T object, String message) {
        Objects.requireNonNull(object, message);
        if (object instanceof String && (((String) object).isEmpty() || ((String) object).isBlank()))
            throw new IllegalArgumentException(message);
    }

    public static <T> void validateURL(T object, String message) {
        Objects.requireNonNull(object, message);
        if (object instanceof String) {
            try {
                URL url = new URL((String) object);
                url.toURI();
            } catch (MalformedURLException | URISyntaxException e) {
                throw new IllegalArgumentException(message + " Root cause " + e.getMessage());
            }
        }

    }

    public static boolean isEmpty(String object) {
        return (object == null || object.isEmpty() || object.isBlank());
    }

    public static boolean hasEmptyElement(String... object) {
        for (String string : object)
            if (string == null || string.isEmpty() || string.isBlank())
                return true;
        return false;
    }

    public static boolean hasEmptyValues(Map<String, String> mapValues) {
        for (Map.Entry<String, String> entry : mapValues.entrySet())
            if (isEmpty(entry.getKey()) || isEmpty(entry.getValue()))
                return true;
        return false;
    }

    public static Set<String> parsePathParamters(String endpointPath) {
        Set<String> pathParams = new HashSet<>();
        // Define a regular expression pattern to match path parameters
        Pattern pattern = Pattern.compile("\\{([^/]+?)\\}");

        // Create a Matcher object to apply the pattern to the endpoint path
        Matcher matcher = pattern.matcher(endpointPath);

        // Iterate over the matches and extract the path parameters
        while (matcher.find()) {
            // Get the path parameter (group 1 of the match)
            String param = matcher.group(1);
            if (pathParams.contains(param))
                throw new IllegalArgumentException("Endpoint path contains duplicate path parameters.");
            pathParams.add(param);
        }
        return pathParams;
    }


}
