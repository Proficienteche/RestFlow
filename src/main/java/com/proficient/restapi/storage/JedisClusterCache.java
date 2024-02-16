package com.proficient.restapi.storage;

import com.proficient.restapi.restclient.CacheManager;
import com.proficient.restapi.restclient.RedisCacheBuilder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.util.Set;

public final class JedisClusterCache implements CacheManager {

    private JedisCluster jedisCache;
    private Set<HostAndPort> hosts;
    private String password;

    public JedisClusterCache(RedisCacheBuilder builder) {
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
        int localMaxConTimeOut = 2000;
        int localMaxAttempts = 5;
        int localSoTimeOut = 2000;

        if (password == null || password.isEmpty())
            jedisCache = new JedisCluster(hosts);
        else
            jedisCache = new JedisCluster(hosts, localMaxConTimeOut, localSoTimeOut, localMaxAttempts, password,
                    new GenericObjectPoolConfig());
    }
}
