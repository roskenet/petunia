package de.roskenet.petunia.villadiana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

@Configuration
public class OidcUserServiceConfig {

    // By default keycloak returns the sub (subject) field as
    // name. This is correct, but not really readable.
    // The CustomOidcUserService uses the login name (preferred_username) instead
    // and also extracts roles from the 'resource_access' claim.
    @Bean
    public OidcUserService oidcUserService() {
        return new CustomOidcUserService();
    }
}
