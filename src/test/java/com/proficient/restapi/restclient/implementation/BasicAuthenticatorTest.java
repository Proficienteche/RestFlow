package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.authenticators.SecureSchemeType;
import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.restclient.Http;
import com.proficient.restapi.restclient.SecurityScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BasicAuthenticatorTest extends RestFlowTestSetup {

    @Test
    @DisplayName("Testing Input data validations.")
    void testInputDataValidation() {
        String userName = "testUser";
        String password = "testPassword";

        /** Testing scheme type*/
        assertThrows(NullPointerException.class, () -> {
            BasicAuthenticator.builder("basic").build();
        });
        /** Testing user name null*/
        assertThrows(NullPointerException.class, () -> {
            BasicAuthenticator.builder("basic").scheme(SecurityScheme.SchemeName.BASIC).build();
        });
        /** Testing username empty value*/
        assertThrows(IllegalArgumentException.class, () -> {
            BasicAuthenticator.builder("basic").scheme(SecurityScheme.SchemeName.BASIC).userName("").build();
        });
        /** Testing password null*/
        assertThrows(NullPointerException.class, () -> {
            BasicAuthenticator.builder("basic").scheme(SecurityScheme.SchemeName.BASIC).userName(userName).build();
        });
        /** Testing password empty*/
        assertThrows(IllegalArgumentException.class, () -> {
            BasicAuthenticator.builder("basic").scheme(SecurityScheme.SchemeName.BASIC).userName(userName).password(
                    "").build();
        });
        /** Testing Bearer token value null*/
        assertThrows(NullPointerException.class, () -> {
            BasicAuthenticator.builder("basic").scheme(SecurityScheme.SchemeName.BEARER).build();
        });
        /** Testing Bearer token value empty*/
        assertThrows(IllegalArgumentException.class, () -> {
            BasicAuthenticator.builder("basic").scheme(SecurityScheme.SchemeName.BEARER).value("").build();
        });

    }

    @Test
    @DisplayName("Testing Basic Security Scheme")
    void testBasicSecurityScheme() {
        SecurityScheme.InType InType = SecurityScheme.InType.HEADER;
        String schemeName = Http.Header.AUTHORIZATION.value();
        SecureSchemeType schemeType = SecureSchemeType.HTTP;
        String authId = "BasicAuthen";

        String expectedHeaderValue = "Basic " + Base64.getEncoder().encodeToString(("testUser:testPassword").getBytes());

        BasicAuthenticator.BasicAuthBuilder builder = BasicAuthenticator.builder(authId)
                .userName("testUser")
                .password("testPassword").clientId("RestClient")
                .scheme(SecurityScheme.SchemeName.BASIC);

        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);

        Authenticator authenticator = builder.build();

        // Verify that addAuthenticator was called with the correct parameters
        verify(mockedClient).addAuthenticator(any(BasicAuthenticator.class));
        assertEquals(expectedHeaderValue, authenticator.getSecurityScheme().getValue());
        assertEquals(schemeName, authenticator.getSecurityScheme().getName());
        assertEquals(InType, authenticator.getSecurityScheme().getInType());
        assertEquals(schemeType, authenticator.getSecurityScheme().getType());
        assertEquals(authId, authenticator.getSecurityScheme().getId());
    }

    @Test
    @DisplayName("Testing Bearer Security scheme")
    void testBearerSecurityScheme() {
        SecurityScheme.InType InType = SecurityScheme.InType.HEADER;
        String schemeName = Http.Header.AUTHORIZATION.value();
        SecureSchemeType schemeType = SecureSchemeType.HTTP;

        String expectedHeaderValue = "Test Bearer key";

        BasicAuthenticator.BasicAuthBuilder builder = BasicAuthenticator.builder("bearer").clientId("RestClient")
                .value(expectedHeaderValue)
                .scheme(SecurityScheme.SchemeName.BEARER);

        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);

        Authenticator authenticator = builder.build();

        // Verify that addAuthenticator was called with the correct parameters
        verify(mockedClient).addAuthenticator(any(BasicAuthenticator.class));
        assertEquals(expectedHeaderValue, authenticator.getSecurityScheme().getValue());
        assertEquals(schemeName, authenticator.getSecurityScheme().getName());
        assertEquals(InType, authenticator.getSecurityScheme().getInType());
        assertEquals(schemeType, authenticator.getSecurityScheme().getType());
    }
}
