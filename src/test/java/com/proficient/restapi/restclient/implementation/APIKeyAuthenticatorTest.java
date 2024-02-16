package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.authenticators.SecureSchemeType;
import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.restclient.SecurityScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class APIKeyAuthenticatorTest extends RestFlowTestSetup{

    @Test
    @DisplayName("Testing API Key Input data validations.")
    void testInputDataValidation() {
        String IdMsg = "Security Scheme reference id is required.";
        String nameMsg = "API Key name should be not be null or empty.";
        String valueMsg = "API Key value should be not be null or empty.";
        String inTypeMsg = "API Key 'In' type should be not be null or empty.";

        /** Testing scheme type*/
        Exception exception = assertThrows(NullPointerException.class, () -> {
            APIKeyAuthenticator.builder("APIKey").build();
        });
        assertEquals(exception.getMessage(), nameMsg);
        /** Testing scheme type*/
        exception = assertThrows(NullPointerException.class, () -> {
            APIKeyAuthenticator.builder("APIKey").clientId("RestClient").build();
        });
        assertEquals(exception.getMessage(), nameMsg);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            APIKeyAuthenticator.builder("APIKey").clientId("RestClient").name("").build();
        });
        assertEquals(exception.getMessage(), nameMsg);

        exception = assertThrows(NullPointerException.class, () -> {
            APIKeyAuthenticator.builder("APIKey").clientId("RestClient").name("x-api-key").build();
        });
        assertEquals(exception.getMessage(), valueMsg);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            APIKeyAuthenticator.builder("APIKey").clientId("RestClient").name("x-api-key").value("").build();
        });
        assertEquals(exception.getMessage(), valueMsg);
        exception = assertThrows(NullPointerException.class, () -> {
            APIKeyAuthenticator.builder("APIKey").clientId("RestClient").name("x-api-key").value("X323").build();
        });
        assertEquals(exception.getMessage(), inTypeMsg);

        exception = assertThrows(NullPointerException.class, () -> {
            APIKeyAuthenticator.builder("APIKey").clientId("RestClient").name("x-api-key").value("X323").inType(null).build();
        });
        assertEquals(exception.getMessage(), inTypeMsg);
    }

    @Test
    @DisplayName("Testing API Key security scheme")
    void testAPIKeySecurityScheme() {
        String value = "X323UIHJH5666JHH";
        String name = "x-API-key";
        SecureSchemeType type = SecureSchemeType.API_KEY;
        SecurityScheme.InType inType = SecurityScheme.InType.HEADER;
        String securitySchemeId = "APIKey";
        APIKeyAuthenticator.APIKeyAuthBuilder apiKeyBuilder = APIKeyAuthenticator.builder(securitySchemeId).clientId(
                "RestClient").name(name).value(value).inType(inType);

        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);
        Authenticator authenticator = apiKeyBuilder.build();

        verify(mockedClient).addAuthenticator(any(APIKeyAuthenticator.class));
        assertEquals(securitySchemeId, authenticator.getSecurityScheme().getId());
        assertEquals(type, authenticator.getSecurityScheme().getType());
        assertEquals(name, authenticator.getSecurityScheme().getName());
        assertEquals(value, authenticator.getSecurityScheme().getValue());
        assertEquals(inType, authenticator.getSecurityScheme().getInType());
    }
}
