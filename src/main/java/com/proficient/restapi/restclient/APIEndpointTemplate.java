package com.proficient.restapi.restclient;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.proficient.restapi.restclient.implementation.APIResponseException;

import java.util.Map;

public interface APIEndpointTemplate {
    /**
     * Sets the endpoint path for this API request template.
     *
     * @param path the endpoint path
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withEndpointPath(String path);

    /**
     * Sets the HTTP method for this API request template.
     *
     * @param method the HTTP method
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withMethod(Http.Method method);

    /**
     * Sets the timeout for this API request template.
     *
     * @param timeout the timeout value
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withTimeout(int timeout);

    /**
     * Sets a path parameter for this API request template.
     *
     * @param pathParamName the name of the path parameter
     * @param pathParamValue the value of the path parameter
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withPathParameter(String pathParamName, String pathParamValue);

    /**
     * Sets path parameters for this API request template.
     *
     * @param pathParameters a map containing path parameter names and values
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withPathParameters(Map<String, String> pathParameters);

    /**
     * Sets path parameters for this API request template using a bean object.
     *
     * @param pathParamsBean an object containing path parameter values
     * @param <T> the type of the bean object
     * @return this APIEndpointTemplate instance
     */
    public <T> APIEndpointTemplate withPathParameters(T pathParamsBean);

    /**
     * Sets a query parameter for this API request template.
     *
     * @param queryParamName the name of the query parameter
     * @param queryParamValue the value of the query parameter
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withQueryParameter(String queryParamName, String queryParamValue);

    /**
     * Sets query parameters for this API request template.
     *
     * @param queryParameters a map containing query parameter names and values
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withQueryParameters(Map<String, String> queryParameters);

    /**
     * Sets query parameters for this API request template using a bean object.
     *
     * @param queryParamsBean an object containing query parameter values
     * @param <T> the type of the bean object
     * @return this APIEndpointTemplate instance
     */
    public <T> APIEndpointTemplate withQueryParameters(T queryParamsBean);

    /**
     * Sets a body parameter for this API request template.
     *
     * @param bodyParamName the name of the body parameter
     * @param bodyParamValue the value of the body parameter
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withBodyParameter(String bodyParamName, String bodyParamValue);

    /**
     * Sets body parameters for this API request template.
     *
     * @param bodyParameters a map containing body parameter names and values
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withBodyParameters(Map<String, String> bodyParameters);

    /**
     * Sets body parameters for this API request template using a bean object.
     *
     * @param bodyParamsBean an object containing body parameter values
     * @param <T> the type of the bean object
     * @return this APIEndpointTemplate instance
     */
    public <T> APIEndpointTemplate withBodyParameters(T bodyParamsBean);

    /**
     * Sets a header for this API request template.
     *
     * @param headerName the name of the header
     * @param headerValue the value of the header
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withHeader(String headerName, String headerValue);

    /**
     * Sets headers for this API request template.
     *
     * @param headers a map containing header names and values
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withHeaders(Map<String, String> headers);

    /**
     * Adds a security scheme ID for this API request template.
     *
     * @param securitySchemeId the ID of the security scheme
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate addSecuritySchemeId(String securitySchemeId);

    /**
     * Adds an authenticator for this API request template.
     *
     * @param authenticator the authenticator to add
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate addAuthenticator(Authenticator authenticator);

    /**
     * Sets the payload for this API request template.
     *
     * @param payload the payload
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withPayload(String payload);

    /**
     * Sets the payload object for this API request template.
     *
     * @param payload the payload object
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withPayload(Object payload);

    /**
     * Sets the property naming strategy for this API request template.
     *
     * @param namingStrategy the property naming strategy
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withPropertyNamingStrategy(PropertyNamingStrategy namingStrategy);


    /**
     * Sets the expected HTTP status for the response to this API request.
     *
     * @param status the expected HTTP status
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withExpectedStatus(Http.Status status);

    /**
     * Sets the expected response type for this API request.
     *
     * @param responseClass the response type class
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate withResponseType(Class<?> responseClass);

    /**
     * Registers this API request template.
     *
     * @return this APIEndpointTemplate instance
     */
    public APIEndpointTemplate registerTemplate();

    /**
     * Creates an API endpoint using this template.
     *
     * @return the created API endpoint
     */
    public APIEndpointTemplate createEndpoint();

// The following method is commented out
//public APIEndpointTemplate createSnapShot(String snapShotName);

    /**
     * Dispatches the API request and returns the response.
     *
     * @param <T> the type of the response
     * @return the response object
     * @throws APIResponseException if an API response exception occurs
     */
    public <T> T dispatch() throws APIResponseException;

    /**
     * Gets the endpoint path for this API request template.
     *
     * @return the endpoint path
     */
    public String getEndpointPath();

}
