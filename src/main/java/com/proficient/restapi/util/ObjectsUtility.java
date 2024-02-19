package com.proficient.restapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ObjectsUtility {

    public static String jsonFormat(String jsonString)
    {
        if(ValidateObjects.isEmpty(jsonString))
                return jsonString;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
           return  writer.writeValueAsString(mapper.readTree(jsonString));
        } catch (JsonProcessingException e) {
            System.out.println("Unable to format input string into JSON");
            return jsonString;
        }
    }
    public static String getStackTraceAsString(Exception e) {
        // Create a StringWriter to store the stack trace
        StringWriter sw = new StringWriter();

        // Create a PrintWriter with the StringWriter
        PrintWriter pw = new PrintWriter(sw);

        // Print the stack trace to the PrintWriter
        e.printStackTrace(pw);

        // Close the PrintWriter to flush the buffer
        pw.close();

        // Return the stack trace as a string
        return sw.toString();
    }
}
