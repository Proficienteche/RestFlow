package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.RESTClient;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

public class RestFlowTestSetup {
    @Mock
    protected RESTClient mockedClient;
    protected static MockedStatic<RestFlow> mockedRestClients;

    @BeforeAll
    static void setUp() {
        if (mockedRestClients == null)
            mockedRestClients = mockStatic(RestFlow.class);
    }

}
