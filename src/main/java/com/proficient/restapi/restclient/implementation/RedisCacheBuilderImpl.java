package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.CacheManager;
import com.proficient.restapi.restclient.RESTClient;
import com.proficient.restapi.restclient.RedisCacheBuilder;
import com.proficient.restapi.storage.JedisCache;
import com.proficient.restapi.storage.JedisClusterCache;
import redis.clients.jedis.HostAndPort;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

final class RedisCacheBuilderImpl implements RedisCacheBuilder {
    private boolean isCluster;
    private Set<HostAndPort> hosts;
    private String password;
    private String clientId;

    public RedisCacheBuilderImpl() {
        this.hosts = new HashSet<>();
        this.isCluster = false;
    }

    @Override
    public RedisCacheBuilder instanceId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    @Override
    public RedisCacheBuilder isCluster(boolean isCluster) {
        this.isCluster = isCluster;
        return this;
    }

    @Override
    public RedisCacheBuilder addNode(String host, int port) {
        hosts.add(new HostAndPort(host, port));
        return this;
    }

    @Override
    public RedisCacheBuilder addNode(String host) {
        String[] server = host.split(":");
        if (server == null || server.length != 2)
            throw new RuntimeException(new IllegalArgumentException("Host format should be in <host>:<port> "));
        hosts.add(new HostAndPort(server[0], Integer.parseInt(server[1])));
        return this;
    }

    @Override
    public RedisCacheBuilder password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public RedisCacheBuilder config(HashMap<String, String> config) {
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Set<HostAndPort> getNodes() {
        return hosts;
    }

    @Override
    public CacheManager build() {
        CacheManager cacheManager = (isCluster) ? new JedisClusterCache(this) : new JedisCache(this);
        if (!clientId.equals(RESTClient.CLIENT_NAME))
            RESTClientEngine.instance().setCacheManager(clientId, cacheManager);
        return cacheManager;
    }

}
