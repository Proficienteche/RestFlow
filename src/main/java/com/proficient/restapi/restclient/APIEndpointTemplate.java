package com.proficient.restapi.restclient;

import com.proficient.restapi.exception.APIResponseException;

import java.util.Map;

public interface APIEndpointTemplate {
    public APIEndpointTemplate withEndpointPath(String path);

    public APIEndpointTemplate withMethod(Http.Method method);

    public APIEndpointTemplate withTimeout(int timeout);

    public APIEndpointTemplate withPathParameter(String pathParamName, String pathParamValue);

    public APIEndpointTemplate withPathParameters(Map<String, String> pathParameters);

    public APIEndpointTemplate withQueryParameter(String queryParamName, String queryParamValue);

    public APIEndpointTemplate withQueryParameters(Map<String, String> queryParameters);

    public APIEndpointTemplate withBodyParameter(String bodyParamName, String bodyParamValue);

    public APIEndpointTemplate withBodyParameters(Map<String, String> bodyParameters);

    public APIEndpointTemplate withHeader(String headerName, String headerValue);

    public APIEndpointTemplate withHeaders(Map<String, String> headers);

    public APIEndpointTemplate addSecuritySchemeId(String securitySchemeId);

    public APIEndpointTemplate addAuthenticator(Authenticator authenticator);

    public APIEndpointTemplate withBody(String body);

    public APIEndpointTemplate withBody(Object body);
    public APIEndpointTemplate withExpectedStatus(Http.Status status);

    public APIEndpointTemplate withResponseType(Class<?> responseClass);

    public APIEndpointTemplate registerTemplate();
    public APIEndpointTemplate createEndpoint();
    public <T> T dispatch() throws APIResponseException;

    public String getEndpointPath();
}
