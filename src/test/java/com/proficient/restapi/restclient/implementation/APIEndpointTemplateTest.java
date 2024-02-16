package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.Http;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class APIEndpointTemplateTest extends RestFlowTestSetup {
    private static String instanceId = "restClient";

    @Test
    @DisplayName("validateEndpointTemplateInputData")
    void validateEndpointTemplateInputData() {

        mockedRestClients.when(() -> RestFlow.instanceOf(anyString())).thenReturn(mockedClient);
        when(mockedClient.getAPISecuritySchemeIds()).thenReturn(new HashSet<>());
        when(mockedClient.getAPIUrl()).thenReturn("https://test.com");
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new APIEndpointTemplateImpl(null);
        });
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new APIEndpointTemplateImpl("").createEndpoint();
        });
        exception = assertThrows(NullPointerException.class, () -> {
            new APIEndpointTemplateImpl(instanceId).withEndpointPath(null).createEndpoint();
        });
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new APIEndpointTemplateImpl(instanceId).withEndpointPath("").createEndpoint();
        });
        exception = assertThrows(NullPointerException.class, () -> {
            new APIEndpointTemplateImpl(instanceId).withEndpointPath("/endpoint/{ip}").createEndpoint();
        });
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new APIEndpointTemplateImpl(instanceId).withEndpointPath("/endpoint/{ip}").withMethod(Http.Method.GET).withPathParameter(
                    "ip1", "parameterValue").createEndpoint();
        });
        assertDoesNotThrow(() -> {
            new APIEndpointTemplateImpl(instanceId).withEndpointPath("/endpoint/{ip}").withMethod(Http.Method.GET).withPathParameter(
                    "ip", "parameterValue").createEndpoint();
        });

    }
}
