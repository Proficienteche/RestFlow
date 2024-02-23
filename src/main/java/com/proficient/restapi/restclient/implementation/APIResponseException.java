package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.exception.APIException;
import com.proficient.restapi.restclient.Http;
import com.proficient.restapi.util.ObjectsUtility;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class APIResponseException extends Exception implements APIException {


    // Instance variables to store HTTP response details
    private String protocol;
    private int statusCode;
    private String statusMessage;
    private String responseBody;
    private String errorTrace;

    private Map<String, List<String>> headers = null;


    private APIResponseException(APIResponseBuilder builder) {
        super(builder.responseBody);
        this.protocol = builder.protocol;
        this.statusCode = builder.statusCode;
        this.statusMessage = Http.messageOf(statusCode);
        this.headers = builder.headers;
        this.responseBody = builder.responseBody;
        this.errorTrace = builder.errorTrace;
    }


    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String statusMessage() {
        return statusMessage;
    }

    @Override
    public Map<String, List<String>> headers() {
        return headers;
    }

    @Override
    public String responseBody() {
        return responseBody;
    }

    @Override
    public String errorTrace() {
        return errorTrace;
    }

    @Override
    public String getMessage() {
        StringBuffer message = new StringBuffer();
        message.append(protocol == null ? "" : protocol);
        message.append(' ');
        message.append(statusCode);
        message.append(' ');
        message.append(statusMessage == null ? "" : statusMessage);
        message.append('\n');
        if (headers != null)
            for (Map.Entry<String, List<String>> entity : headers.entrySet())
                message.append(entity.getKey()).append(": ").append(String.join(",", entity.getValue())).append('\n');
        if (responseBody != null) {
            //format response body if the content type is application/json
            if (isJSONContent())
                message.append('\n').append(ObjectsUtility.jsonFormat(responseBody));
            else
                message.append('\n').append(responseBody);
        }
        if (errorTrace != null)
            message.append('\n').append('\n').append(errorTrace);
        return message.toString();
    }

    @Override
    public String toString() {
        return getMessage();
    }


    static APIResponseBuilder APIResponseBuilder() {
        return new APIResponseBuilder();
    }

    static APIResponseBuilder APIResponseBuilder(HttpResponse<String> response, Exception exception) {
        APIResponseBuilder builder = new APIResponseBuilder();
        if (response != null)
            builder.protocol(response.version().name()).
                    statusCode(response.statusCode()).
                    headers(response.headers().map()).
                    responseBody(response.body());
        if(exception != null)
            builder.errorTrace(ObjectsUtility.getStackTraceAsString(exception));
        if(exception != null && response==null)
            builder.responseBody(exception.getMessage());
        return builder;
    }

    private boolean isJSONContent() {
        String contentType = Http.Header.CONTENT_TYPE.caseInsensitiveValue();
        String jsonContentType = Http.ContentType.APPLICATION_JSON.value();

        if (headers.containsKey(contentType)) {
            List<String> headerValues = headers.get(contentType);
            for (String contentTypeValue : headerValues)
                if (contentTypeValue.contains(jsonContentType))
                    return true;
        }
        return false;
    }

    static class APIResponseBuilder {
        private String protocol;
        private int statusCode;

        private String statusMessage;
        private String responseBody;
        private String errorTrace;

        private Map<String, List<String>> headers = null;

        APIResponseBuilder() {

        }

        APIResponseBuilder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        APIResponseBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        APIResponseBuilder headers(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        APIResponseBuilder responseBody(String responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        APIResponseBuilder errorTrace(String errorTrace) {
            this.errorTrace = errorTrace;
            return this;
        }

        APIResponseException build() {
            return new APIResponseException(this);
        }

    }
}
