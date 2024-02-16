package com.proficient.restapi.storage;

import com.proficient.restapi.restclient.CacheManager;
import com.proficient.restapi.restclient.RedisCacheBuilder;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Set;

public final class JedisCache implements CacheManager {

    private Jedis jedisCache;
    private Set<HostAndPort> hosts;
    private String password;

    public JedisCache(RedisCacheBuilder builder) {
        this.password = builder.getPassword();
        this.hosts = builder.getNodes();
        init();
    }

    @Override
    public CacheManager init() {
        connect();
        return this;
    }

    @Override
    public void set(String key, String value) {
        jedisCache.set(key, value);
    }

    @Override
    public void set(String key, String value, int ttl) {
        jedisCache.set(key, value);
        jedisCache.expire(key, ttl);
    }

    @Override
    public void expire(String key, int ttl) {
        jedisCache.expire(key, ttl);
    }

    @Override
    public String get(String key) {
        return jedisCache.get(key);
    }

    @Override
    public void remove(String key) {
        jedisCache.del(key);
    }

    @Override
    public void clearCache() {

    }

    private void connect() {
        HostAndPort node = null;
        if (hosts.size() >= 1)
            node = (HostAndPort) hosts.toArray()[0];
        jedisCache = new Jedis(node);
        if (password != null && !password.isEmpty())
            jedisCache.auth(password);
    }
}
