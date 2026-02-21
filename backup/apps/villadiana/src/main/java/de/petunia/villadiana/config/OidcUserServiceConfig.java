package de.petunia.villadiana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

//@Configuration
public class OidcUserServiceConfig {

    // By default keycloak returns the sub (subject) field as
    // name. This is correct, but not really readable.
    // The CustomOidcUserService uses the login name instead.
    @Bean
    public OidcUserService oidcUserService() {
        return new CustomOidcUserService();
    }
}
