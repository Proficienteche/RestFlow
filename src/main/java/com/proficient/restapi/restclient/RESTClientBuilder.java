package com.proficient.restapi.restclient;

import com.proficient.restapi.authenticators.AuthenticationBuilder;

import java.util.Map;

public interface RESTClientBuilder {

    public <T> RESTClientBuilder loadEndPointBuilders(T openAPISpec);

//    public RESTClientBuilder cacheManager(CacheManager cache);

    public RESTClientBuilder addAPIEndpointTemplate(APIEndpointTemplate endpointTemplate);
    public RESTClientBuilder addAuthenticator(Authenticator authenticator);
    public RESTClientBuilder addAuthBuilder(AuthenticationBuilder authBuilder);

    public RESTClient createInstance();

    public Map<String, APIEndpointTemplate> apiEndpointTemplates();
    public Map<String, Authenticator> authenticators();
    public Map<String, AuthenticationBuilder> authenticationBuilders();
//    public CacheManager cacheManager();

    public String getAPIUrl();



}
