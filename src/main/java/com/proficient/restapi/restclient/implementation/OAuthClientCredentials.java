package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.authenticators.ClientCredentialsBuilder;
import com.proficient.restapi.restclient.Http;
import com.proficient.restapi.util.ValidateObjects;

import java.net.URI;
import java.util.HashMap;

public class OAuthClientCredentials {
    private String clientId;
    private String clientSecret;
    private String scope;
    private URI tokenUrl;
    private boolean isBasicAuth;
    private HashMap<String, String> headers = null;

    OAuthClientCredentials(ClientCredentialsBuilder builder) {
        ClientCredentialsBuilderImpl ClientBuilder = (ClientCredentialsBuilderImpl) builder;
        this.clientId = ClientBuilder.clientId;
        this.clientSecret = ClientBuilder.clientSecret;
        this.scope = ClientBuilder.scope;
        this.tokenUrl = URI.create(ClientBuilder.tokenUrl);
        this.isBasicAuth = ClientBuilder.isBasicAuth;
        this.headers = ClientBuilder.headers;
    }

    String getClientId() {
        return clientId;
    }

    String getClientSecret() {
        return clientSecret;
    }

    String getScope() {
        return scope;
    }

    URI getTokenUrl() {
        return tokenUrl;
    }

    boolean isBasicAuth() {
        return isBasicAuth;
    }

    HashMap<String, String> getHeaders() {
        return headers;
    }

    public static ClientCredentialsBuilderImpl builder(String id) {
        return new ClientCredentialsBuilderImpl().id(id);
    }

    static class ClientCredentialsBuilderImpl implements ClientCredentialsBuilder {

        private String id;
        private String instanceId;
        private String clientId;
        private String clientSecret;
        private String scope;
        private String tokenUrl;
        private boolean isBasicAuth;
        private HashMap<String, String> headers = null;

        private ClientCredentialsBuilderImpl() {
            this.isBasicAuth = true;
            this.headers = new HashMap<>();
            headers.put(Http.Header.CONTENT_TYPE.value(), Http.ContentType.X_WWW_FORM_URLENCODED.value());
        }

        @Override
        public String getClientId() {
            return instanceId;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public ClientCredentialsBuilderImpl instanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        @Override
        public ClientCredentialsBuilderImpl id(String id) {
            this.id = id;
            return this;
        }


        @Override
        public ClientCredentialsBuilderImpl clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Override
        public ClientCredentialsBuilderImpl clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        @Override
        public ClientCredentialsBuilderImpl scope(String scope) {
            this.scope = scope;
            return this;
        }

        @Override
        public ClientCredentialsBuilderImpl tokenUrl(String tokenUrl) {
            this.tokenUrl = tokenUrl;
            return this;
        }

        @Override
        public ClientCredentialsBuilderImpl isBasicAuth(boolean isBasicAuth) {
            this.isBasicAuth = isBasicAuth;
            return this;
        }

        @Override
        public ClientCredentialsBuilderImpl header(String headerName, String value) {
            this.headers.put(headerName, value);
            return this;
        }

        @Override
        public ClientCredentialsBuilderImpl headers(HashMap<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Authenticator build() {
            //validate input data
            validateInput();
            OAuthAuthenticator oAuthenticator = new OAuthAuthenticator(this);
            RestFlow.instanceOf(instanceId).addAuthenticator(oAuthenticator);
            return oAuthenticator;
        }

        private void validateInput() {
            ValidateObjects.mandatory(id,"Security Scheme reference id is required.");
            ValidateObjects.mandatory(instanceId,"Rest client reference id is required.");
            ValidateObjects.mandatory(clientId,"Client ID is required for OAuth client credentials grant type.");
            ValidateObjects.mandatory(clientSecret,"Client Secret is required for OAuth client credentials grant " +
                    "type.");
            ValidateObjects.mandatory(tokenUrl,"Token URL is required to obtain access token.");
            ValidateObjects.validateURL(tokenUrl,"URL is invalid.");
        }
    }


}
