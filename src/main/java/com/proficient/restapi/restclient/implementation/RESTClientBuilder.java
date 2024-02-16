package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.authenticators.AuthenticationBuilder;
import com.proficient.restapi.restclient.APIEndpointTemplate;
import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.restclient.CacheManager;
import com.proficient.restapi.restclient.RESTClient;
import com.proficient.restapi.storage.InMemoryCache;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class RESTClientBuilder implements com.proficient.restapi.restclient.RESTClientBuilder {

    private Map<String, AuthenticationBuilder> authenticationBuilders = null;
    private Map<String, APIEndpointTemplate> apiEndpointTemplates = null;
    private Map<String, Authenticator> authenticators = null;

    private String apiURL = null;

    RESTClientBuilder(String url) {
        authenticationBuilders = new HashMap<>();
        apiEndpointTemplates = new HashMap<>();
        authenticators = new HashMap<>();
        this.apiURL = url;
    }

    @Override
    public <T> com.proficient.restapi.restclient.RESTClientBuilder loadEndPointBuilders(T openAPISpec) {
        if (!(openAPISpec instanceof String || openAPISpec instanceof File || openAPISpec instanceof InputStream))
            throw new IllegalArgumentException("Invalid object type. Allowable object types are String, File, or InputStream.");
        String apiSpec = null;
        try {
            if (openAPISpec instanceof InputStream) {
                BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) openAPISpec));
                apiSpec = reader.lines().toString();
            } else if (openAPISpec instanceof File) {
                BufferedReader reader = null;
                reader = new BufferedReader(new FileReader((File) openAPISpec));
                apiSpec = reader.lines().toString();
            } else {
                apiSpec = (String) openAPISpec;
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unable to load Open API Specification. Root cause -" + e.getMessage());
        }
        return this;
    }


    @Override
    public com.proficient.restapi.restclient.RESTClientBuilder addAPIEndpointTemplate(APIEndpointTemplate endpointTemplate) {
        this.apiEndpointTemplates.put(endpointTemplate.getEndpointPath(), endpointTemplate);
        return this;
    }

    @Override
    public com.proficient.restapi.restclient.RESTClientBuilder addAuthenticator(Authenticator authenticator) {
        authenticators.put(authenticator.securitySchemeId(), authenticator);
        return this;
    }

    @Override
    public com.proficient.restapi.restclient.RESTClientBuilder addAuthBuilder(AuthenticationBuilder authenticator) {
        authenticationBuilders.put(authenticator.getId(), authenticator);
        return this;
    }

    @Override
    public Map<String, APIEndpointTemplate> apiEndpointTemplates() {
        return apiEndpointTemplates;
    }

    @Override
    public Map<String, Authenticator> authenticators() {
        return authenticators;
    }

    @Override
    public Map<String, AuthenticationBuilder> authenticationBuilders() {
        return authenticationBuilders;
    }

    @Override
    public String getAPIUrl() {
        return apiURL;
    }

    @Override
    public RESTClient createInstance() {
        return new RESTClientImpl(this);
    }

}
