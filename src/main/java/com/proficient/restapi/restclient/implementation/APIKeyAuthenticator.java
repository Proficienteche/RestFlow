package com.proficient.restapi.restclient.implementation;


import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.authenticators.APIKeyAuthenticatorBuilder;
import com.proficient.restapi.authenticators.SecureSchemeType;
import com.proficient.restapi.restclient.SecurityScheme;
import com.proficient.restapi.util.ValidateObjects;

class APIKeyAuthenticator implements Authenticator {

    private String schemeId;
    private SecurityScheme securityScheme;

    APIKeyAuthenticator(APIKeyAuthBuilder builder) {
        this.schemeId = builder.getId();
        this.securityScheme = builder.securityScheme;
    }

    @Override
    public String securitySchemeId() {
        return schemeId;
    }

    @Override
    public SecurityScheme getSecurityScheme() {
        return securityScheme;
    }

    static APIKeyAuthBuilder builder(String securitySchemeId) {
        return new APIKeyAuthBuilder().id(securitySchemeId);
    }

    static class APIKeyAuthBuilder implements APIKeyAuthenticatorBuilder {

        private String clientId;
        private String id;
        private String name;
        private String value;
        private SecurityScheme.InType inType;

        private SecurityScheme securityScheme;


        private SecureScheme.SecuritySchemeBuilder schemeBuilder;

        private APIKeyAuthBuilder() {
            schemeBuilder = SecureScheme.builder();
            schemeBuilder.type(SecureSchemeType.API_KEY);
        }
        @Override
        public APIKeyAuthBuilder clientId(String instanceId) {
            this.clientId = instanceId;
            return this;
        }

        @Override
        public APIKeyAuthBuilder id(String id) {
            this.id = id;
            return this;
        }

        @Override
        public String getClientId() {
            return clientId;
        }

        @Override
        public String getId() {
            return id;
        }
        @Override
        public APIKeyAuthBuilder name(String name) {
            this.name = name;
            return this;
        }
        @Override
        public APIKeyAuthBuilder value(String value) {
            this.value = value;
            return this;
        }
        @Override
        public APIKeyAuthBuilder inType(SecurityScheme.InType inType) {
            this.inType = inType;
            return this;
        }
        public Authenticator build() {
            validateInput();
            securityScheme = schemeBuilder.build();
            APIKeyAuthenticator authenticator = new APIKeyAuthenticator(this);
            RestFlow.instanceOf(clientId).addAuthenticator(authenticator);
            return authenticator;
        }
        private void validateInput()
        {
            ValidateObjects.mandatory(id,"Security Scheme reference id is required.");
            ValidateObjects.mandatory(name,"API Key name should be not be null or empty.");
            ValidateObjects.mandatory(value,"API Key value should be not be null or empty.");
            ValidateObjects.mandatory(inType,"API Key 'In' type should be not be null or empty.");
            schemeBuilder.id(id);
            schemeBuilder.name(name);
            schemeBuilder.value(value);
            schemeBuilder.inType(inType);
        }

    }
}
