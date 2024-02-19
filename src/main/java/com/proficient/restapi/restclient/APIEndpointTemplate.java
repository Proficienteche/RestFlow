package com.proficient.restapi.restclient;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.proficient.restapi.restclient.implementation.APIResponseExceptionImpl;

import java.util.Map;

public interface APIEndpointTemplate {
    public APIEndpointTemplate withEndpointPath(String path);

    public APIEndpointTemplate withMethod(Http.Method method);

    public APIEndpointTemplate withTimeout(int timeout);

    public APIEndpointTemplate withPathParameter(String pathParamName, String pathParamValue);

    public APIEndpointTemplate withPathParameters(Map<String, String> pathParameters);

    public <T> APIEndpointTemplate withPathParameters(T pathParamsBean);

    public APIEndpointTemplate withQueryParameter(String queryParamName, String queryParamValue);

    public APIEndpointTemplate withQueryParameters(Map<String, String> queryParameters);

    public <T> APIEndpointTemplate withQueryParameters(T queryParamsBean);

    public APIEndpointTemplate withBodyParameter(String bodyParamName, String bodyParamValue);

    public APIEndpointTemplate withBodyParameters(Map<String, String> bodyParameters);

    public <T> APIEndpointTemplate withBodyParameters(T bodyParamsBean);

    public APIEndpointTemplate withHeader(String headerName, String headerValue);

    public APIEndpointTemplate withHeaders(Map<String, String> headers);

    public APIEndpointTemplate addSecuritySchemeId(String securitySchemeId);

    public APIEndpointTemplate addAuthenticator(Authenticator authenticator);

    public APIEndpointTemplate withPayload(String payload);

    public APIEndpointTemplate withPayload(Object payload);

    public APIEndpointTemplate withPropertyNamingStrategy(PropertyNamingStrategy namingStrategy);

    public APIEndpointTemplate withExpectedStatus(Http.Status status);

    public APIEndpointTemplate withResponseType(Class<?> responseClass);

    public APIEndpointTemplate registerTemplate();

    public APIEndpointTemplate createEndpoint();

//    public APIEndpointTemplate createSnapShot(String snapShotName);

    public <T> T dispatch() throws APIResponseExceptionImpl;

    public String getEndpointPath();
}
