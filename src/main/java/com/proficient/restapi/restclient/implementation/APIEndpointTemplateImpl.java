package com.proficient.restapi.restclient.implementation;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proficient.restapi.exception.APIResponseException;
import com.proficient.restapi.restclient.*;
import com.proficient.restapi.util.ValidateObjects;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

final class APIEndpointTemplateImpl implements APIEndpointTemplate {

    private String instanceId;
    private String path;
    private Http.Method method;
    private HashMap<String, String> pathParams;
    private HashMap<String, String> queryParams;
    private HashMap<String, String> bodyParams;
    private HashMap<String, String> headers;
    private String body;
    private int timeout = 15;
    private Http.Status expectedStatus;
    private Class responseClass;
    private HttpRequest httpRequest;
    private List<String> securitySchemeIds;
    private HashMap<String, Authenticator> authenticators;

    private boolean isDefaultResponse;


    APIEndpointTemplateImpl(String instanceId) {
        ValidateObjects.mandatory(instanceId, "Valid RestClient instance reference id is required.");
        this.instanceId = instanceId;
        restRequestParams();
    }

    private void restRequestParams() {
        pathParams = new HashMap<>();
        queryParams = new HashMap<>();
        bodyParams = new HashMap<>();
        headers = new HashMap<>();
        securitySchemeIds = new ArrayList<>();
        authenticators = new HashMap<>();
        headers.put(Http.Header.CONTENT_TYPE.value(), Http.ContentType.APPLICATION_JSON.value());
    }

    @Override
    public APIEndpointTemplate withEndpointPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public APIEndpointTemplate withMethod(Http.Method method) {
        this.method = method;
        return this;
    }

    @Override
    public APIEndpointTemplate withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public APIEndpointTemplate withPathParameter(String parameterName, String parameterValue) {
        if (!ValidateObjects.hasEmptyElement(parameterName, parameterValue))
            pathParams.put(parameterName, parameterValue);
        return this;
    }

    @Override
    public APIEndpointTemplate withPathParameters(Map<String, String> pathParameters) {
        if (!ValidateObjects.hasEmptyValues(pathParameters))
            pathParams.putAll(pathParameters);
        return this;
    }

    @Override
    public APIEndpointTemplate withQueryParameter(String parameterName, String parameterValue) {
        if (!ValidateObjects.hasEmptyElement(parameterName, parameterValue))
            queryParams.put(parameterName, parameterValue);
        return this;
    }

    @Override
    public APIEndpointTemplate withQueryParameters(Map<String, String> queryParameters) {
        if (!ValidateObjects.hasEmptyValues(queryParameters))
            queryParams.putAll(queryParameters);
        return this;
    }

    @Override
    public APIEndpointTemplate withBodyParameter(String parameterName, String parameterValue) {
        if (!ValidateObjects.hasEmptyElement(parameterName, parameterValue))
            bodyParams.put(parameterName, parameterValue);
        return this;
    }

    @Override
    public APIEndpointTemplate withBodyParameters(Map<String, String> bodyParameters) {
        if (!ValidateObjects.hasEmptyValues(bodyParameters))
            bodyParams.putAll(bodyParameters);
        return this;
    }

    @Override
    public APIEndpointTemplate withHeader(String headerName, String headerValue) {
        if (!ValidateObjects.hasEmptyElement(headerName, headerValue))
            headers.put(headerName, headerValue);
        return this;
    }

    @Override
    public APIEndpointTemplate withHeaders(Map<String, String> headers) {
        if (!ValidateObjects.hasEmptyValues(headers))
            this.headers.putAll(headers);
        return this;
    }

    @Override
    public APIEndpointTemplate addSecuritySchemeId(String securitySchemeId) {
        securitySchemeIds.add(securitySchemeId);
        return this;
    }

    @Override
    public APIEndpointTemplate addAuthenticator(Authenticator authenticator) {
        authenticators.put(authenticator.securitySchemeId(), authenticator);
        addSecuritySchemeId(authenticator.securitySchemeId());
        return this;
    }

    @Override
    public APIEndpointTemplate withBody(String body) {
        if (!ValidateObjects.isEmpty(body))
            this.body = body;
        return this;
    }

    @Override
    public APIEndpointTemplate withBody(Object body) {
        try {
            this.body = new ObjectMapper().writeValueAsString(body);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse object into JSON string. " + e.getMessage());
        }
        return this;
    }

    @Override
    public APIEndpointTemplate withExpectedStatus(Http.Status status) {
        this.expectedStatus = status;
        return this;
    }

    @Override
    public APIEndpointTemplate withResponseType(Class<?> responseClass) {
        this.responseClass = responseClass;
        return this;
    }

    @Override
    public APIEndpointTemplate registerTemplate() {
        return this;
    }

    @Override
    public APIEndpointTemplate createEndpoint() {
        validateInputData();
        buildHttpRequest();

        return this;
    }

    @Override
    public <T> T dispatch() throws APIResponseException {
        HttpResponse<String> response = null;
        try {
            setResponseType();
            HttpClient httpClient = RESTClientEngine.instance().getHttpClient(instanceId);
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (isDefaultResponse)
                return (T) new EndpointResponse(response.statusCode(), response.body());
            else if (response.statusCode() == expectedStatus.code()) {
                if (!responseClass.equals(String.class))
                    return (T) new ObjectMapper().readValue(response.body(), responseClass);
                else
                    return (T) response.body();
            } else
                throw new APIResponseException(response.version().name(), response.statusCode(),
                        response.headers().map(), response.body());
        } catch (JsonMappingException e) {
            throw new APIResponseException(response.version().name(), response.statusCode(),
                    response.headers().map(), response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getEndpointPath() {
        return path;
    }

    private void validateInputData() {
        ValidateObjects.mandatory(path, "Endpoint path is required.");
        ValidateObjects.mandatory(method, "Http Method is required");
        Set<String> pathParameters = ValidateObjects.parsePathParamters(path);
        if (pathParameters.size() > 0) {
            Set<String> givenPathParams = pathParams.keySet();
            if (givenPathParams == null || !givenPathParams.containsAll(pathParameters))
                throw new IllegalArgumentException("Required path parameters are missed.");
        }
    }

    private void setResponseType() {
        //reset default response value
        isDefaultResponse = false;
        if (expectedStatus == null && responseClass == null) {
            isDefaultResponse = true;
        } else if (expectedStatus == null && responseClass != null) {
            expectedStatus = Http.Status.OK;
        } else if (expectedStatus != null && responseClass == null) {
            responseClass = String.class;
        }
    }

    private void buildHttpRequest() {

        HttpRequest.Builder requestBuilder = null;
        HttpRequest.BodyPublisher bodyPublisher = null;
        String endPointPath = path;
        String bodyForm = null;

        setSecuritySchemes();

        if (bodyParams.size() > 0)
            bodyForm = prepareBodyForm();
        if (method == Http.Method.POST || method == Http.Method.PUT || method == Http.Method.PATCH) {
            if (ValidateObjects.isEmpty(body))
                this.body = bodyForm;
            bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
        } else
            bodyPublisher = HttpRequest.BodyPublishers.noBody();


        if (pathParams.size() > 0)
            endPointPath = replaceParameters(endPointPath);
        if (queryParams.size() > 0)
            endPointPath = prepareQueryString(endPointPath);

        RESTClient restClient = RestFlow.instanceOf(instanceId);
        URI uri = URI.create(restClient.getAPIUrl() + endPointPath);
        requestBuilder =
                HttpRequest.newBuilder(uri).method(method.getName(), bodyPublisher).headers(getHeaders()).timeout(Duration.ofSeconds(timeout));

        httpRequest = requestBuilder.build();
    }

    private void setSecuritySchemes() {
        List<Authenticator> authenticatorList = new ArrayList<>();
        RESTClient restClient = RestFlow.instanceOf(instanceId);
        if (securitySchemeIds.size() > 0) {
            for (String id : securitySchemeIds)
                authenticatorList.add(authenticators.containsKey(id) ? authenticators.get(id)
                        : restClient.getAuthenticator(id));
        } else {
            for (String id : restClient.getAPISecuritySchemeIds())
                authenticatorList.add(restClient.getAuthenticator(id));
        }
        for (Authenticator authenticator : authenticatorList) {
            SecurityScheme securityScheme = authenticator.getSecurityScheme();
            if (securityScheme.getInType() == SecurityScheme.InType.HEADER)
                headers.put(securityScheme.getName(), securityScheme.getValue());
            else if (securityScheme.getInType() == SecurityScheme.InType.QUERY)
                queryParams.put(securityScheme.getName(), securityScheme.getValue());
        }

    }


    private String replaceParameters(String path) {
        String endPointPath = path;
        try {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                String param = "{" + entry.getKey() + "}";
                if (endPointPath.contains(param)) {
                    endPointPath = endPointPath.replace(param, URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to encode URL path parameter value." + e.getMessage());
        }
        return endPointPath;
    }

    private String prepareQueryString(String path) {
        StringBuffer queryString = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String param = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
                String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                if (queryString.length() > 0)
                    queryString.append("&");
                queryString.append(param).append('=').append(value);
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to encode query parameters." + e.getMessage());
        }
        return path + "?" + queryString.toString();
    }

    private String prepareBodyForm() {
        StringBuffer formData = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                String param = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
                String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                if (formData.length() > 0)
                    formData.append("&");
                formData.append(param).append('=').append(value);
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to encode form parameters." + e.getMessage());
        }
        return formData.toString();
    }

    private String[] getHeaders() {
        String[] headerList = new String[headers.size() * 2];
        Set<String> headerNames = headers.keySet();
        int i = 0;
        for (String headerName : headerNames) {
            headerList[i++] = headerName;
            headerList[i++] = headers.get(headerName);
        }
        return headerList;
    }
}