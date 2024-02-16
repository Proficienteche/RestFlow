package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.authenticators.ClientCredentialsBuilder;
import com.proficient.restapi.authenticators.SecureSchemeType;
import com.proficient.restapi.restclient.Http;
import com.proficient.restapi.restclient.SecurityScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OAuthClientCredentialsTest extends RestFlowTestSetup {

    @Test
    @DisplayName("Testing client credentials input data validations")
    void testClientCredentialsInputValidation() {
        String idMsg = "Security Scheme reference id is required.";
        String instanceIdMsg = "Rest client reference id is required.";
        String clientIdMsg = "Client ID is required for OAuth client credentials grant type.";
        String clientSecretMsg = "Client Secret is required for OAuth client credentials grant type.";
        String tokenUrl = "Token URL is required to obtain access token.";
        String invalidUrl = "URL is invalid.";

        Exception exception = assertThrows(NullPointerException.class, () -> {
            OAuthClientCredentials.builder(null).build();
        });
        assertEquals(idMsg, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            OAuthClientCredentials.builder("").build();
        });
        assertEquals(idMsg, exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> {
            OAuthClientCredentials.builder("Oauth").build();
        });
        assertEquals(instanceIdMsg, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("").build();
        });
        assertEquals(instanceIdMsg, exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId(null).build();
        });
        assertEquals(clientIdMsg, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("").build();
        });
        assertEquals(clientIdMsg, exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").build();
        });
        assertEquals(clientSecretMsg, exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").clientSecret(null).build();
        });
        assertEquals(clientSecretMsg, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").clientSecret("").build();
        });
        assertEquals(clientSecretMsg, exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").clientSecret("Test-Secret").build();
        });

        assertEquals(tokenUrl, exception.getMessage());
        exception = assertThrows(NullPointerException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").clientSecret("Test-Secret").tokenUrl(null).build();
        });
        assertEquals(tokenUrl, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").clientSecret("Test-Secret").tokenUrl("").build();
        });
        assertEquals(tokenUrl, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").clientSecret("Test-Secret").tokenUrl(
                    "http:\\ups.com").build();
        });
        assertTrue(exception.getMessage().contains(invalidUrl));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            OAuthClientCredentials.builder("Oauth").instanceId("restClient").clientId("Test-ClientID").clientSecret("Test-Secret").tokenUrl(
                    "http://tesps.com\\rest.com").build();
        });
        assertTrue(exception.getMessage().contains(invalidUrl));

    }

    @Test
    @DisplayName("Testing Client Credentials")
    void testClientCredentials() {
        ClientCredentialsBuilder builder =
                OAuthClientCredentials.builder("Oauth").instanceId("restclient").clientId("Test-ClientID").clientSecret(
                        "Test-Secret").tokenUrl("http://tesps.com//rest.com");

        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);

        OAuthAuthenticator authenticator = spy((OAuthAuthenticator) builder.build());
        doNothing().when(authenticator).resetAccessToken();
        verify(mockedClient).addAuthenticator(any(OAuthAuthenticator.class));
        assertEquals(authenticator.getSecurityScheme().getName(), Http.Header.AUTHORIZATION.value());
        assertEquals(authenticator.getSecurityScheme().getType(), SecureSchemeType.OAUTH_2);
        assertEquals(authenticator.getSecurityScheme().getInType(), SecurityScheme.InType.HEADER);
        assertEquals(authenticator.getSecurityScheme().getId(), "Oauth");
    }
}
