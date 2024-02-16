package com.proficient.restapi.authenticators;

/**
 * Represents the type of security scheme used in API authentication.
 */
public enum SecureSchemeType {

    /**
     * API Key security scheme.
     */
    API_KEY,

    /**
     * HTTP security scheme.
     */
    HTTP,

    /**
     * OAuth 2.0 security scheme.
     */
    OAUTH_2,

    /**
     * OpenID Connect security scheme.
     */
    OPEN_ID_CONNECT;
}
