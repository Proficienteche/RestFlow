package com.proficient.restapi.restclient;

import com.proficient.restapi.authenticators.SecureSchemeType;

/**
 * Represents a security scheme used in API authentication and authorization.
 */
public interface SecurityScheme {

    /**
     * Retrieves the unique identifier of the security scheme.
     *
     * @return The ID of the security scheme.
     */
    public String getId();

    /**
     * Retrieves the type of the security scheme.
     *
     * @return The type of the security scheme.
     */
    public SecureSchemeType getType();

    /**
     * Retrieves the name of the security scheme.This applies only to SecuritySchemeType.HTTP
     *
     * @return The name of the security scheme.
     */
    public SchemeName getScheme();

    /**
     * Retrieves the display name of the security scheme.This applies only to SecuritySchemeType.APIKEY
     *
     * @return The display name of the security scheme.
     */
    public String getName();

    /**
     * Retrieves the type of the input location for the security scheme value.
     *
     * @return The type of the input location.
     */
    public InType getInType();

    /**
     * Retrieves the value of the security scheme.
     *
     * @return The value of the security scheme.
     */
    public String getValue();

    /**
     * Setting token value. This method is used to set authorization token for OAuth2.0 client credentials grant typ
     *
     * @param value token value
     */
    public void setValue(String value);

    /**
     * Represents the name of the Security Scheme type HTTP
     */
    public enum SchemeName {
        BASIC, BEARER;
    }

    /**
     * Represents the type of input location for the APIKEY type security scheme value.
     */
    public enum InType {
        QUERY, HEADER, COOKIE;
    }


}

