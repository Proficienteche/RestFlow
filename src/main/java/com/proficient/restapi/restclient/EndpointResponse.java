package com.proficient.restapi.restclient;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.proficient.restapi.util.ObjectsUtility;

public class EndpointResponse {
    private int statusCode;
    private String statusDescription;
    @JsonRawValue
    private String body;

    public EndpointResponse() {
    }

    public EndpointResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.statusDescription = Http.messageOf(statusCode);
        this.body = ObjectsUtility.jsonFormat(body);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.statusDescription = Http.messageOf(statusCode);
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body =ObjectsUtility.jsonFormat(body);
    }
    @Override
    public String toString()
    {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
