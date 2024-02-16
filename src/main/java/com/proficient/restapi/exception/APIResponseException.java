package com.proficient.restapi.exception;

import com.proficient.restapi.restclient.Http;

import java.util.List;
import java.util.Map;

public class APIResponseException extends Exception {


    // Instance variables to store HTTP response details
    private String protocol;
    private int statusCode;

    private String statusMessage;
    private String responseBody;

    private Map<String, List<String>> headers = null;


    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public APIResponseException() {
        super("Unable to prepare endpoint response.");
    }


    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param responseBody the detail message. The detail message is saved for
     *                     later retrieval by the {@link #getMessage()} method.
     */
    public APIResponseException(String responseBody) {
        super(responseBody);
        this.responseBody = responseBody;
    }

    public APIResponseException(String protocol, int statusCode, Map<String,
            List<String>> headers, String responseBody) {
        super(responseBody);
        this.protocol = protocol;
        this.statusCode = statusCode;
        this.statusMessage = Http.messageOf(statusCode);
        this.headers = headers;
        this.responseBody = responseBody;
    }

    public int statusCode() {
        return statusCode;
    }

    public String responseBody() {
        return responseBody;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public String protocol() {
        return protocol;
    }

    public String statusMessage() {
        return statusMessage;
    }

    public String getMessage() {
        StringBuffer message = new StringBuffer();
        message.append(protocol + " " + statusCode +" " + statusMessage + "\n");
        for (Map.Entry<String, List<String>> entity : headers.entrySet())
            message.append(entity.getKey() + ": " + String.join(",", entity.getValue()) + "\n");
        message.append("\n" + responseBody);
        return message.toString();
    }
    public String toString()
    {
        return getMessage();
    }
}
