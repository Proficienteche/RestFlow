package com.proficient.restapi.restclient.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proficient.restapi.authenticators.ClientCredentialsBuilder;
import com.proficient.restapi.restclient.Http;
import com.proficient.restapi.storage.InMemoryCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OAuthAuthenticatorTest extends RestFlowTestSetup {

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";

    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpClient.Builder httpClientBuilder;
    private OAuthAuthenticator authenticator = null;
    private ClientCredentialsBuilder builder = null;
    private static String instanceId = "restClient";

    private String clientId = "Test-ClientID";
    private String clientSecret = "Test-Secret";


    @BeforeAll
    static void setUp() {
        RESTClientEngine.instance().setCacheManager(instanceId, new InMemoryCache().init());
    }

    @BeforeEach
    void init() {
        builder =
                OAuthClientCredentials.builder("Oauth").instanceId(instanceId).clientId(clientId).clientSecret(
                        clientSecret).tokenUrl("http://tesps.com//rest.com");
        RESTClientEngine.instance().getCacheManager(instanceId).clearCache();
    }

    @AfterEach
    void tearDown() {
        builder = null;
    }


    @Test
    void testHeaders() {
        String[] expectedHeaders = {Http.Header.CONTENT_TYPE.value(), Http.ContentType.X_WWW_FORM_URLENCODED.value()};

        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);

        authenticator = spy((OAuthAuthenticator) builder.build());
        String[] headers = authenticator.getHeaders();
        assertArrayEquals(expectedHeaders, headers);
    }

    @Test
    void testPostBody() {
        String grantType = URLEncoder.encode(GRANT_TYPE, StandardCharsets.UTF_8);
        String credentials = URLEncoder.encode(CLIENT_CREDENTIALS, StandardCharsets.UTF_8);

        String clientID = URLEncoder.encode(clientId, StandardCharsets.UTF_8);
        String secret = URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);
        StringBuffer bodyData = new StringBuffer(grantType + "=" + credentials);


        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);

        authenticator = spy((OAuthAuthenticator) builder.build());
        String postBody = authenticator.getPostBody();

        assertEquals(bodyData.toString(), postBody);

        builder.isBasicAuth(false);
        bodyData.append("&client_id=").append(clientID).append("&client_secret=").append(secret);
        authenticator = spy((OAuthAuthenticator) builder.build());
        postBody = authenticator.getPostBody();

        assertEquals(bodyData.toString(), postBody);
    }

    @Test
    void testAuthenticator() {
        AccessToken token = new AccessToken();
        String accessToken = token.getAccess_token();

        mockStatic(HttpClient.class).when(() -> HttpClient.newBuilder()).thenReturn(httpClientBuilder);
        when(httpClientBuilder.connectTimeout(any())).thenReturn(httpClientBuilder);
        when(httpClientBuilder.build()).thenReturn(httpClient);
        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);

        try {
            authenticator = spy((OAuthAuthenticator) builder.build());

            HttpResponse<String> response = mock(HttpResponse.class);
            when(response.statusCode()).thenReturn(200);
            when(response.body()).thenReturn(token.toString());
            when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn((HttpResponse<String>) response);
            assertEquals("Bearer " + token.access_token, authenticator.getSecurityScheme().getValue());
            token.setAccessToken("temparary tokens");
            assertEquals("Bearer " + accessToken, authenticator.getSecurityScheme().getValue());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class AccessToken {
        private String token_type = "Bearer";
        private String issued_at = "1707916113261";
        private String client_id = "ANcCdVEZL6Zg7Ydu9Akh2CXCoxVL1mlwCKbAobrUeqanNhsA";
        private String access_token = "eyJraWQiOiI2NGM0YjYyMC0yZmFhLTQzNTYtYjA0MSANcCdVEZL6Zg7Ydu9Akh2CXCoxVL1mlwCKbAobrUeqanNhsA";
        private String expires_in = "14399";
        private String status = "approved";

        public String getToken_type() {
            return token_type;
        }

        public void setAccessToken(String accessToken) {
            access_token = accessToken;
        }

        public String getIssued_at() {
            return issued_at;
        }

        public String getClient_id() {
            return client_id;
        }

        public String getAccess_token() {
            return access_token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public String getStatus() {
            return status;
        }

        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
