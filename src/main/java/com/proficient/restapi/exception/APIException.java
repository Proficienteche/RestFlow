package com.proficient.restapi.exception;

import java.util.List;
import java.util.Map;

public interface APIException {
    public String protocol();

    public int statusCode();

    public String statusMessage();

    public Map<String, List<String>> headers();

    public String responseBody();

    public String errorTrace();
}
