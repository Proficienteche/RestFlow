package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.CacheManager;
import com.proficient.restapi.restclient.RESTClient;
import com.proficient.restapi.restclient.RedisCacheBuilder;
import com.proficient.restapi.util.ValidateObjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RestFlow {

    private static Map<String, RESTClient> restClients = new HashMap<>();

    private RestFlow() {
    }

    public static <T> RESTClient createInstance(String restURL, String apiContext, T openAPISpec) {

        ValidateObjects.mandatory(restURL, "RestAPI URL should not be null");
        apiContext = (apiContext == null) ? "" : apiContext;
        String url = restURL + apiContext;
        ValidateObjects.validateURL(url, "Invalid Rest API URL.");

        String clientRefKey = RestClientHelper.getHashString(url);

        com.proficient.restapi.restclient.RESTClientBuilder restClientBuilder = new RESTClientBuilder(url);
        if (openAPISpec != null)
            restClientBuilder.loadEndPointBuilders(openAPISpec);
        RESTClient restClient = restClientBuilder.createInstance(false);

        restClients.put(clientRefKey, restClient);

        return restClient;
    }
    public static RESTClient createInstance(String restURL, String apiContext) {
        return createInstance(restURL, apiContext, null);
    }
    public static RESTClient createInstance(String restURL, String apiContext,String redisHost, int redisPort) {
        ValidateObjects.mandatory(redisHost,"Redis host should not be null or empty.");
        Objects.requireNonNull(redisPort,"Redis port should not be null or empty.");

        RESTClient restClient = createInstance(restURL, apiContext, null);

        RedisCacheBuilder cacheBuilder =   restClient.getCacheBuilderOf(CacheManager.CacheType.REDIS);

        cacheBuilder.addNode(redisHost,redisPort).isCluster(false).build();

        return restClient;
    }

    public static <T> RESTClient transientRestClient(String restURL, String apiContext) {

        ValidateObjects.mandatory(restURL, "RestAPI URL should not be null");
        apiContext = (apiContext == null) ? "" : apiContext;
        String url = restURL + apiContext;
        ValidateObjects.validateURL(url, "Invalid Rest API URL.");

        com.proficient.restapi.restclient.RESTClientBuilder restClientBuilder = new RESTClientBuilder(url);

        RESTClient restClient = restClientBuilder.createInstance(true);

        return restClient;
    }



    public static RESTClient instanceOf(String instanceId) {
        return restClients.get(instanceId);
    }

    public static void shutdownRestClient(String instanceId) {
        RESTClient client = restClients.get(instanceId);
        client = null;
        restClients.remove(instanceId);

    }

}
