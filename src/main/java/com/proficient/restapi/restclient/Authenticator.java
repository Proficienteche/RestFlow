package com.proficient.restapi.restclient;

public interface Authenticator {


    public String securitySchemeId();

    public SecurityScheme getSecurityScheme();

//    public Object clone() throws CloneNotSupportedException;
}
