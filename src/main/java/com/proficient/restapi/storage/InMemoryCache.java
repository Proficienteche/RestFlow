package com.proficient.restapi.storage;

import com.proficient.restapi.restclient.CacheManager;

import java.util.HashMap;

public final class InMemoryCache implements CacheManager {

    private HashMap<String, String> cache = null;
    private HashMap<String, Long> keyttl = null;

    @Override
    public CacheManager init() {
        cache = new HashMap<>();
        keyttl = new HashMap<>();
        return this;
    }

    @Override
    public void set(String key, String value) {
        cache.put(key, value);
    }

    @Override
    public void set(String key, String value, int ttl) {
        cache.put(key, value);
        if (ttl > -1)
            keyttl.put(key, (System.currentTimeMillis() / 1000) + ttl);
    }

    @Override
    public void expire(String key, int ttl) {
        keyttl.put(key, (System.currentTimeMillis() / 1000) + ttl);
    }

    @Override
    public String get(String key) {
        if (keyttl.containsKey(key) && (System.currentTimeMillis() / 1000) >= keyttl.get(key))
            cache.remove(key);
        return cache.get(key);
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
        keyttl.remove(key);
    }

    @Override
    public void clearCache() {
        cache = new HashMap<>();
        keyttl = new HashMap<>();
    }
}
