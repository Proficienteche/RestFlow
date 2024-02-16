package com.proficient.restapi.restclient;

import redis.clients.jedis.HostAndPort;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface RedisCacheBuilder {

    public RedisCacheBuilder instanceId(String clientId);

    public RedisCacheBuilder isCluster(boolean isCluster);

    public RedisCacheBuilder addNode(String host, int port);

    public RedisCacheBuilder addNode(String host);

    public RedisCacheBuilder password(String password);

    public RedisCacheBuilder config(HashMap<String, String> config);

    public String getPassword();

    public Set<HostAndPort> getNodes();

    public void build();
}
