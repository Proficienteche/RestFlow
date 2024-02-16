package com.proficient.restapi.restclient;

public interface Authenticator {


    public String securitySchemeId();
    public SecurityScheme getSecurityScheme();
}
