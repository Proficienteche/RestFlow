package com.proficient.restapi.restclient.implementation;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class RestClientHelper {
    private static final String SECRET_KEY = "HashKey@0912873465";
    public static String getHashString(String input) {
       return getHashString(input,SECRET_KEY);
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
}
