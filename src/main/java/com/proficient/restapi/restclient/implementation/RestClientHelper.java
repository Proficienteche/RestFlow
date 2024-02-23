package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.Http;
import com.proficient.restapi.util.ObjectsUtility;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class RestClientHelper {
    private static final String SECRET_KEY = "HashKey@0912873465";

    public static String getHashString(String input) {
        return getHashString(input, SECRET_KEY);
    }

    public static String getHashString(String input, String secretKey) {
        Mac hmac = null;
        try {
            hmac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
            hmac.init(secretKeySpec);

            byte[] hashedBytes = hmac.doFinal(input.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            //log the exception specify the details
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            //log the exception specify the details
            e.printStackTrace();
        }
        return null;
    }

    static APIResponseException getUnableToConnectException(Exception exception) {
        String errorDetails = "{\"response\":{\"errors\":[{\"code\":\"10001\",\"message\":\"Unable to connect " +
                "API server. Please check network or host details.\"}]}}";
        var headers = new HashMap<String, List<String>>();
        var headerValue = new ArrayList<String>();
        headerValue.add(Http.ContentType.APPLICATION_JSON.value());
        headers.put(Http.Header.CONTENT_TYPE.value(), headerValue);
        APIResponseException.APIResponseBuilder exceptionBuilder =
                APIResponseException.APIResponseBuilder();
        exceptionBuilder.
                statusCode(Http.Status.UNABLE_TO_CONNECT.code()).
                headers(headers).
                responseBody(errorDetails).
                errorTrace(ObjectsUtility.getStackTraceAsString(exception));
        return exceptionBuilder.build();
    }
}
