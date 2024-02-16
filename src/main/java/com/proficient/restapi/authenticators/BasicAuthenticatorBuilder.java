package com.proficient.restapi.authenticators;

import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.restclient.SecurityScheme;

public interface BasicAuthenticatorBuilder extends AuthenticationBuilder{
    public BasicAuthenticatorBuilder clientId(String clientId);

    public BasicAuthenticatorBuilder id(String id);
    public BasicAuthenticatorBuilder scheme(SecurityScheme.SchemeName scheme);

    public BasicAuthenticatorBuilder value(String value);

    public BasicAuthenticatorBuilder userName(String userName) ;

    public BasicAuthenticatorBuilder password(String password) ;

    public Authenticator build();
}
