package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.authenticators.AuthenticationBuilder;
import com.proficient.restapi.authenticators.SecureSchemeType;
import com.proficient.restapi.restclient.*;
import com.proficient.restapi.storage.InMemoryCache;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class RESTClientImpl implements RESTClient {


    private String apiURL = null;
    private Set<String> securityIds = null;
    private Map<String, Authenticator> authenticators = null;
    private Map<String, AuthenticationBuilder> authBuilders = null;
    private Map<String, APIEndpointTemplate> apiEndpointBuilders = null;
    private String instanceId;

    RESTClientImpl(RESTClientBuilder restBuilder) {
        this.apiURL = restBuilder.getAPIUrl();
        this.authBuilders = restBuilder.authenticationBuilders();
        this.authenticators = restBuilder.authenticators();
        this.apiEndpointBuilders = restBuilder.apiEndpointTemplates();
        this.instanceId = RestClientHelper.getHashString(apiURL);
        this.securityIds = authenticators.size() > 0 ? authenticators.keySet() : new HashSet<>();
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        RESTClientEngine.instance().setClientDetails(instanceId, httpClient, new InMemoryCache().init());
    }

    /**
     * Returns API server URL
     * <p>
     * This method returns the URL of the API that the client should use to interact with the system.
     * The URL may include the protocol (e.g., HTTP or HTTPS), hostname, port, and context path.
     *
     * @return Returns API server base url.
     */
    @Override
    public String getAPIUrl() {
        return apiURL;
    }

    /**
     * Returns the ID of the running RESTClient instance.
     * <p>
     * This method retrieves the unique identifier (ID) associated with the current
     * instance of the RESTClient. The instance ID can be used to distinguish
     * between different instances of the RESTClients within the application.
     *
     * @return The ID of the running RESTClient instance.
     */
    @Override
    public String getInstanceId() {
        return instanceId;
    }


    /**
     * Adds a security scheme authenticator to the RestClient.
     *
     * @param authenticator The authenticator to be added to the RestClient.
     */
    @Override
    public void addAuthenticator(Authenticator authenticator) {
        securityIds.add(authenticator.securitySchemeId());
        authenticators.put(authenticator.securitySchemeId(), authenticator);
    }

    /**
     * Returns an Authenticator instance tailored to handle authentication and retrieval of
     * access keys, API keys, or credentials based on the security scheme identified by the provided ID.
     *
     * @param securitySchemeId The ID of the security scheme for which to retrieve the Authenticator.
     * @return An Authenticator instance for the specified security scheme.
     */
    @Override
    public Authenticator getAuthenticator(String securitySchemeId) {
        return authenticators.get(securitySchemeId);
    }

    /**
     * Retrieves the IDs of the API security schemes applied to all API endpoints.
     * <p>
     * This method returns a list of IDs of the API security schemes that are applied to all endpoints
     * in the API. These security scheme IDs define the authentication and authorization mechanisms
     * required to access the API endpoints.
     *
     * @return A list of API security scheme IDs.
     */
    @Override
    public Set<String> getAPISecuritySchemeIds() {
        return securityIds;
    }

    /**
     * Returns a Security Scheme builder tailored for the specified scheme type. The builder is used
     * to construct scheme authenticators, and the resulting Authenticator is registered within the RestClient.
     *
     * @param securitySchemeType The type of security scheme for which to retrieve the builder.
     * @return A Security Scheme builder for the specified scheme type.
     */
    @Override
    public <T> T securitySchemeBuilderOf(SecureSchemeType securitySchemeType) {

        if (securitySchemeType == SecureSchemeType.API_KEY)
            return (T) APIKeyAuthenticator.builder("apiKey").clientId(instanceId);
        else if (securitySchemeType == SecureSchemeType.HTTP)
            return (T) BasicAuthenticator.builder("http").clientId(instanceId);
        else if (securitySchemeType == SecureSchemeType.OAUTH_2)
            return (T) OAuthClientCredentials.builder("oauth2").instanceId(instanceId);
        else
            throw new IllegalArgumentException("Security scheme 'OPEN_ID_CONNECT' does not support.");
    }

    /**
     * Returns a Security Scheme builder tailored for the specified scheme id. The builder is used
     * to construct scheme authenticators, and the resulting Authenticator is registered within the RestClient.
     *
     * @param securitySchemeId The type of security scheme id to retrieve the builder.
     * @return A Security Scheme builder for the specified scheme id.
     */
    @Override
    public <T> T securitySchemeBuilderOf(String securitySchemeId) {
        return (T) authBuilders.get(securitySchemeId);
    }


    @Override
    public <T> T getCacheBuilderOf(CacheManager.CacheType cacheType) {
        if (cacheType == CacheManager.CacheType.REDIS)
            return (T) new RedisCacheBuilderImpl().instanceId(instanceId);
        throw new IllegalArgumentException("Redis cache only supported.");
    }


    @Override
    public void setCacheManager(CacheManager cacheManager) {
        RESTClientEngine.instance().setCacheManager(instanceId, cacheManager);
    }

    /**
     * Returns an empty APIEndpointTemplate, which is used to prepare endpoint requests
     * and make calls to the API server.
     *
     * @return An APIEndpointTemplate instance for preparing and calling API server endpoint.
     */
    @Override
    public APIEndpointTemplate createEndpointTemplate() {
      return new APIEndpointTemplateImpl(instanceId);
    }

    /**
     * Returns an APIEndpointTemplate instance with basic required endpoint data, such as the API endpoint path.
     * The client can then configure any remaining dynamic data and use the template to create an endpoint request to call
     * the API server.
     *
     * @param apiEndpointPath The path of the API endpoint.
     * @return An APIEndpointTemplate instance.
     */
    @Override
    public APIEndpointTemplate getEndpointTemplateOf(String apiEndpointPath) {
        return apiEndpointBuilders.get(apiEndpointPath);
    }

    @Override
    public void registerEndpointTemplate(APIEndpointTemplate apiEndpointTemplate) {
        apiEndpointBuilders.put(apiEndpointTemplate.getEndpointPath(), apiEndpointTemplate);
    }
}
