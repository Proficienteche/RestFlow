package com.proficient.restapi.restclient;

/**
 * SecuritySchemeManager to set the cache details to hold the security scheme.
 *
 * @author Your Name
 * @since Date
 */
public interface SecuritySchemeManager {

    /**
     * Sets the cache manager for this security scheme manager.
     *
     * @param cache the cache manager to set
     */
    public void setCacheManager(CacheManager cache);
}

