package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.CacheManager;

import java.net.http.HttpClient;
import java.util.HashMap;

class RESTClientEngine {

    private static RESTClientEngine clientEngine = new RESTClientEngine();
    private HashMap<String, clientDetails> clientDetails = null;

    private RESTClientEngine() {
        clientDetails = new HashMap<>();
    }

    static RESTClientEngine instance() {
        return clientEngine;
    }

    void setClientDetails(String id, HttpClient httpClient, CacheManager cacheManager) {
        clientDetails.put(id, new clientDetails(id, httpClient, cacheManager));
    }

    void setCacheManager(String id, CacheManager cacheManager) {
        if (clientDetails.containsKey(id))
            clientDetails.get(id).setCacheManager(cacheManager);
        else
            clientDetails.put(id, new clientDetails(id, null, cacheManager));
    }
    void setHttpClient(String id, HttpClient httpClient) {
        if (clientDetails.containsKey(id))
            clientDetails.get(id).setHttpclient(httpClient);
        else
            clientDetails.put(id, new clientDetails(id, httpClient, null));
    }
    CacheManager getCacheManager(String id)
    {
        if(clientDetails.containsKey(id))
           return clientDetails.get(id).getCacheManager();
        else
            return null;
    }
    HttpClient getHttpClient(String id)
    {
        if(clientDetails.containsKey(id))
            return clientDetails.get(id).getHttpclient();
        else
            return null;
    }
    private class clientDetails {
        private String clientId;
        private CacheManager cacheManager;
        private HttpClient httpclient;

        public clientDetails(String clientId, HttpClient httpclient, CacheManager cacheManager) {
            this.clientId = clientId;
            this.cacheManager = cacheManager;
            this.httpclient = httpclient;
        }

        private String getClientId() {
            return clientId;
        }

        private void setClientId(String clientId) {
            this.clientId = clientId;
        }

        private CacheManager getCacheManager() {
            return cacheManager;
        }

        private void setCacheManager(CacheManager cacheManager) {
            this.cacheManager = cacheManager;
        }

        private HttpClient getHttpclient() {
            return httpclient;
        }

        private void setHttpclient(HttpClient httpclient) {
            this.httpclient = httpclient;
        }
    }
}
