package com.proficient.restapi.authenticators;

import com.proficient.restapi.restclient.Authenticator;

import java.util.HashMap;

public interface ClientCredentialsBuilder extends AuthenticationBuilder {

    public ClientCredentialsBuilder instanceId(String instanceId);

    public ClientCredentialsBuilder id(String id);

    public ClientCredentialsBuilder clientId(String client_id);

    public ClientCredentialsBuilder clientSecret(String client_secret);

    public ClientCredentialsBuilder scope(String scope);

    public ClientCredentialsBuilder tokenUrl(String tokenUrl);

    public ClientCredentialsBuilder isBasicAuth(boolean isBasicAuth);

    public ClientCredentialsBuilder header(String headerName, String value);

    public ClientCredentialsBuilder headers(HashMap<String, String> headers);

    public Authenticator build();
}
