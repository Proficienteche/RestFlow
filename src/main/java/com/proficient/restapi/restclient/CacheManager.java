package com.proficient.restapi.restclient;

public interface CacheManager {

    public CacheManager init();
    public void set(String key, String value);

    public void set(String key, String value, int ttl);

    public void expire(String key, int ttl);
    public String get(String key);
    public void remove(String key);
    public void clearCache();

    public enum CacheType{
        REDIS;
    }

}
