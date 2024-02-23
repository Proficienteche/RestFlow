package com.proficient.restapi.restclient;

import com.proficient.restapi.authenticators.SecureSchemeType;

import java.util.Set;

public interface RESTClient {
    public static final String CLIENT_NAME="TRANSIENT_CLIENT";
    /**
     * Returns API server URL
     * <p>
     * This method returns the URL of the API that the client should use to interact with the system.
     * The URL may include the protocol (e.g., HTTP or HTTPS), hostname, port, and context path.
     *
     * @return Returns API server base url.
     */
    public String getAPIUrl();

    /**
     * Returns the ID of the running RESTClient instance.
     * <p>
     * This method retrieves the unique identifier (ID) associated with the current
     * instance of the RESTClient. The instance ID can be used to distinguish
     * between different instances of the RESTClients within the application.
     *
     * @return The ID of the running RESTClient instance.
     */
    public String getInstanceId();

    /**
     * Returns an APIEndpointTemplate, which is used to prepare endpoint requests
     * and make calls to the API server.
     *
     * @return An APIEndpointTemplate instance for preparing and calling API server endpoint.
     */
    public APIEndpointTemplate createEndpointTemplate();

    /**
     * Returns an APIEndpointTemplate instance with basic required endpoint data, such as the API endpoint path.
     * The client can then configure any remaining dynamic data and use the template to create an endpoint request to call
     * the API server.
     *
     * @param apiEndpointPath The path of the API endpoint.
     * @return An APIEndpointTemplate instance.
     */
    public APIEndpointTemplate getEndpointTemplateOf(String apiEndpointPath);

    /**
     * Adds an APIEndpointTemplate to the internal endpoint list.
     * This method is used to include a new API endpoint template for future use in making API calls.
     *
     * @param apiEndpointTemplate The APIEndpointTemplate instance to be added.
     */
    public void registerEndpointTemplate(APIEndpointTemplate apiEndpointTemplate);

    /**
     * Returns a Security Scheme builder tailored for the specified scheme type. The builder is used
     * to construct security scheme and the resulting Authenticator is registered within the RestClient.
     *
     * @param securitySchemeType The type of security scheme for which to retrieve the builder.
     * @param <T>                The type of the Security Scheme builder.
     * @return A Security Scheme builder for the specified scheme type.
     */
    public <T> T securitySchemeBuilderOf(SecureSchemeType securitySchemeType);

    /**
     * Adds a security scheme authenticator to the list API authenticators.
     *
     * @param authenticator The authenticator to be added to the RestClient.
     */
    public void addAuthenticator(Authenticator authenticator);

    /**
     * Returns an Authenticator instance tailored to handle authentication and retrieval of
     * access keys, API keys, or credentials based on the security scheme identified by the provided ID.
     *
     * @param securitySchemeId The ID of the security scheme for which to retrieve the Authenticator.
     * @return An Authenticator instance for the specified security scheme.
     */
    public Authenticator getAuthenticator(String securitySchemeId);

    /**
     * Retrieves the IDs of the API security schemes applied to all API endpoints.
     * <p>
     * This method returns a list of IDs of the API security schemes that are applied to all endpoints
     * in the API. These security scheme IDs define the authentication and authorization mechanisms
     * required to access the API endpoints.
     * </p>
     *
     * @return A list of API security scheme IDs.
     */
    public Set<String> getAPISecuritySchemeIds();

    /**
     * Returns a Security Scheme builder tailored for the specified scheme id. The builders are registered by
     * loading static details from OpenAPIv3.0 specification.
     *
     * <p>Client configures remaining details and build authenticator. </p>
     *
     * @param securitySchemeId The type of security scheme id to retrieve the builder.
     * @param <T>              The type of the Security Scheme builder.
     * @return A Security Scheme builder for the specified scheme id.
     */
    public <T> T securitySchemeBuilderOf(String securitySchemeId);

    /**
     * Set Cache Manager
     * @param cacheManager
     */
    public void setCacheManager(CacheManager cacheManager);

    /**
     * Get Cache builder. It support Redis cache and in-memory cache
     * @param cacheType
     * @return
     * @param <T>
     */
    public <T> T getCacheBuilderOf(CacheManager.CacheType cacheType);

}
