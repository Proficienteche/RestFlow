package com.proficient.restapi.restclient.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proficient.restapi.restclient.*;
import com.proficient.restapi.authenticators.ClientCredentialsBuilder;
import com.proficient.restapi.authenticators.SecureSchemeType;
import com.proficient.restapi.util.ValidateObjects;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Set;

final class OAuthAuthenticator implements Authenticator, SecuritySchemeManager {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_IN = "expires_in";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";

    private OAuthClientCredentials clientCredentials;
    private String schemeId;
    private String keyRefId;
    private String clientId;
    private SecurityScheme securityScheme;
    private CacheManager cache;

    OAuthAuthenticator(ClientCredentialsBuilder builder) {
        this.clientCredentials = new OAuthClientCredentials(builder);
        this.schemeId = builder.getId();
        this.clientId = builder.getClientId();
        this.securityScheme =
                SecureScheme.builder().name(Http.Header.AUTHORIZATION.value()).id(schemeId).type(SecureSchemeType.OAUTH_2).
                        inType(SecurityScheme.InType.HEADER).build();

        this.keyRefId = RestClientHelper.getHashString(clientCredentials.getClientId() + ":"
                + clientCredentials.getClientSecret() + ":" + clientCredentials.getTokenUrl(), clientCredentials.getClientSecret());
    }

    private OAuthAuthenticator() {

    }

    @Override
    public String securitySchemeId() {
        return schemeId;
    }

    @Override
    public SecurityScheme getSecurityScheme() {
        resetAccessToken();
        return securityScheme;
    }

    @Override
    public void setCacheManager(CacheManager cache) {
        this.cache = cache;
    }

//    @Override
//    public Object clone() {
//        OAuthAuthenticator authenticator = new OAuthAuthenticator();
//        authenticator.clientId = this.clientId;
//        authenticator.keyRefId = this.keyRefId;
//        authenticator.schemeId = this.schemeId;
//        try {
//            authenticator.securityScheme = (SecurityScheme) this.securityScheme.clone();
//            authenticator.clientCredentials = (OAuthClientCredentials) this.clientCredentials.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException(e);
//        }
//        return authenticator;
//    }

    protected void resetAccessToken() {
        String accessToken = null;
        if (cache != null)
            accessToken = cache.get(keyRefId);
        else
            accessToken =RESTClientEngine.instance().getCacheManager(clientId).get(keyRefId);

        if (ValidateObjects.isEmpty(accessToken))
            accessToken = authenticate();
        securityScheme.setValue(accessToken);
    }

    protected String authenticate() {
        System.out.println("Getting access token");
        try {
            setAuthHeader();
            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(clientCredentials.getTokenUrl())
                    .headers(getHeaders()).POST(HttpRequest.BodyPublishers.ofString(getPostBody()))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == Http.Status.OK.code())
                return getJWTToken(response.body());
            else
                throw new RuntimeException(APIResponseException.
                        APIResponseBuilder(response, null).
                        build());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    protected void setAuthHeader() {
        if (clientCredentials.isBasicAuth()) {
            String basicAuthCode = "Basic " + Base64.getEncoder().encodeToString((URLEncoder.encode(clientCredentials.getClientId(),
                    StandardCharsets.UTF_8) + ":" + URLEncoder.encode(clientCredentials.getClientSecret(), StandardCharsets.UTF_8)).getBytes());
            clientCredentials.getHeaders().put(Http.Header.AUTHORIZATION.value(), basicAuthCode);
        }
    }

    protected String[] getHeaders() {
        String[] headerList = new String[clientCredentials.getHeaders().size() * 2];
        Set<String> headerNames = clientCredentials.getHeaders().keySet();
        int i = 0;
        for (String headerName : headerNames) {
            headerList[i++] = headerName;
            headerList[i++] = clientCredentials.getHeaders().get(headerName);
        }
        return headerList;
    }

    protected String getPostBody() {
        String grantType = URLEncoder.encode(GRANT_TYPE, StandardCharsets.UTF_8);
        String credentials = URLEncoder.encode(CLIENT_CREDENTIALS, StandardCharsets.UTF_8);
        StringBuffer bodyData = new StringBuffer(grantType + "=" + credentials);
        if (!clientCredentials.isBasicAuth()) {
            String clientID = URLEncoder.encode(clientCredentials.getClientId(), StandardCharsets.UTF_8);
            String clientSecret = URLEncoder.encode(clientCredentials.getClientSecret(), StandardCharsets.UTF_8);
            bodyData.append("&client_id=").append(clientID).append("&client_secret=").append(clientSecret);
        }
        return bodyData.toString();
    }

    private int graceTime = 180;

    protected String getJWTToken(String accessToken) {
        StringBuffer jwtToken = new StringBuffer("Bearer ");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(accessToken);
            jwtToken.append(jsonNode.get(ACCESS_TOKEN).asText());
            int expiryTime = jsonNode.get(EXPIRES_IN).asInt();
            expiryTime = (expiryTime > graceTime) ? (expiryTime - graceTime) : expiryTime;
            /**
             * Get Cache Manager from REST Client and set token
             */
            if (cache != null)
                cache.set(keyRefId, jwtToken.toString(), expiryTime);
            else
                RESTClientEngine.instance().getCacheManager(clientId).set(keyRefId, jwtToken.toString(), expiryTime);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(APIResponseException.APIResponseBuilder(null, e).build());
        }
        return jwtToken.toString();
    }


}
