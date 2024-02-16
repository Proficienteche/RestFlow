package com.proficient.restapi.restclient.implementation;

import com.proficient.restapi.restclient.Authenticator;
import com.proficient.restapi.restclient.Http;
import com.proficient.restapi.authenticators.BasicAuthenticatorBuilder;
import com.proficient.restapi.authenticators.SecureSchemeType;
import com.proficient.restapi.restclient.SecurityScheme;
import com.proficient.restapi.util.ValidateObjects;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class BasicAuthenticator implements Authenticator {

    private String schemeId;
    private SecurityScheme securityScheme;

    BasicAuthenticator(BasicAuthBuilder builder) {
        this.schemeId = builder.id;
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

    static BasicAuthBuilder builder(String securitySchemeId) {
        return new BasicAuthBuilder().id(securitySchemeId);
    }

    static class BasicAuthBuilder implements BasicAuthenticatorBuilder {

        private String id;
        private String clientId;
        private String value;
        private String userName;
        private String password;
        private SecurityScheme securityScheme;
        private SecurityScheme.SchemeName scheme;

        private SecureScheme.SecuritySchemeBuilder schemeBuilder;

        private BasicAuthBuilder() {
            schemeBuilder = SecureScheme.builder();
            schemeBuilder.type(SecureSchemeType.HTTP).inType(SecurityScheme.InType.HEADER).name(Http.Header.AUTHORIZATION.value());
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
        public BasicAuthBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Override
        public BasicAuthBuilder id(String id) {
            this.id = id;
            schemeBuilder.id(id);
            return this;
        }

        @Override
        public BasicAuthBuilder scheme(SecurityScheme.SchemeName scheme) {
            this.scheme = scheme;
            schemeBuilder.scheme(scheme);
            return this;
        }

        @Override
        public BasicAuthBuilder value(String value) {
            this.value = value;
            schemeBuilder.value(value);
            return this;
        }

        @Override
        public BasicAuthBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        @Override
        public BasicAuthBuilder password(String password) {
            this.password = password;
            return this;
        }

        @Override
        public Authenticator build() {
            ValidateObjects.mandatory(id, "Security Scheme reference id is required.");
            ValidateObjects.mandatory(scheme, "Security Scheme is required.");
            if (scheme == SecurityScheme.SchemeName.BASIC)
                setHeaderValue();
            else if (scheme == SecurityScheme.SchemeName.BEARER)
                ValidateObjects.mandatory(value, "Token is required for Bearer security scheme.");
            securityScheme = schemeBuilder.build();
            BasicAuthenticator basicAuth = new BasicAuthenticator(this);
            RestFlow.instanceOf(clientId).addAuthenticator(basicAuth);
            return basicAuth;
        }

        private void setHeaderValue() {
            ValidateObjects.mandatory(userName, "UserName is required.");
            ValidateObjects.mandatory(password, "Password is required.");

            String basicAuthCode = "Basic " + Base64.getEncoder().encodeToString((URLEncoder.encode(userName,
                    StandardCharsets.UTF_8) + ":" + URLEncoder.encode(password, StandardCharsets.UTF_8)).getBytes());

            schemeBuilder.value(basicAuthCode);
        }
    }
}
