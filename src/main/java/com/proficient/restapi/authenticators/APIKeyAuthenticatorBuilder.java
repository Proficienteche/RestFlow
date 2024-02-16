package com.proficient.restapi.authenticators;

import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.restclient.SecurityScheme;

public interface APIKeyAuthenticatorBuilder extends AuthenticationBuilder {


    public APIKeyAuthenticatorBuilder clientId(String clientId);

    public APIKeyAuthenticatorBuilder id(String id);
    public APIKeyAuthenticatorBuilder name(String name);

    public APIKeyAuthenticatorBuilder value(String value);

    public APIKeyAuthenticatorBuilder inType(SecurityScheme.InType inType);

    public Authenticator build();
}
