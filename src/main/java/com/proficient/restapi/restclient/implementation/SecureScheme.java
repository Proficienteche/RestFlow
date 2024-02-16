package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.SecurityScheme;
import com.proficient.restapi.authenticators.SecureSchemeType;

class SecureScheme implements SecurityScheme {

    private String id;
    private String name;
    private String value;
    private SecureSchemeType type;
    private InType inType;
    private SchemeName scheme;

    SecureScheme(SecuritySchemeBuilder builder)
    {
       this.id = builder.id;
       this.name = builder.name;
       this.value = builder.value;
       this.type = builder.type;
       this.scheme = builder.scheme;
       this.inType = builder.inType;
    }

    /**
     * Retrieves the unique identifier of the security scheme.
     *
     * @return The ID of the security scheme.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Retrieves the type of the security scheme.
     *
     * @return The type of the security scheme.
     */
    @Override
    public SecureSchemeType getType() {
        return type;
    }

    /**
     * Retrieves the name of the security scheme.This applies only to SecuritySchemeType.HTTP
     *
     * @return The name of the security scheme.
     */
    @Override
    public SchemeName getScheme() {
        return scheme;
    }

    /**
     * Retrieves the display name of the security scheme.This applies only to SecuritySchemeType.APIKEY
     *
     * @return The display name of the security scheme.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the type of the input location for the security scheme value.
     *
     * @return The type of the input location.
     */
    @Override
    public InType getInType() {
        return inType;
    }

    /**
     * Retrieves the value of the security scheme.
     *
     * @return The value of the security scheme.
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Setting token value. This method is used to set authorization token for OAuth2.0 client credentials grant typ
     *
     * @param value token value
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }


    static SecuritySchemeBuilder builder() {
        return new SecureScheme.SecuritySchemeBuilder();
    }

    static class SecuritySchemeBuilder {

        private String id;
        private String name;
        private String value;
        private SecureSchemeType type;
        private SecurityScheme.InType inType;
        private SecurityScheme.SchemeName scheme;

        private SecuritySchemeBuilder() {
        }
        SecuritySchemeBuilder id(String id)
        {
            this.id = id;
            return this;
        }
        SecuritySchemeBuilder name(String name)
        {
            this.name = name;
            return this;
        }
        SecuritySchemeBuilder value(String value)
        {
            this.value = value;
            return this;
        }
        SecuritySchemeBuilder type(SecureSchemeType type)
        {
            this.type = type;
            return this;
        }
        SecuritySchemeBuilder inType(SecurityScheme.InType inType)
        {
            this.inType = inType;
            return this;
        }
        SecuritySchemeBuilder scheme(SecurityScheme.SchemeName scheme)
        {
            this.scheme = scheme;
            return this;
        }
        SecurityScheme build()
        {
            //Validate input data and create security scheme object
            return new SecureScheme(this);
        }
    }

}
