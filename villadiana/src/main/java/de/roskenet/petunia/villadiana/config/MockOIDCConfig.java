package de.roskenet.petunia.villadiana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Configuration
@Profile({"dev", "test"})
public class MockOIDCConfig {

    @Bean
    @Primary
    public OidcUser mockOidcUser() {
        OidcIdToken idToken = new OidcIdToken(
            "token",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            Map.of(
                "sub", "user123",
                "email", "dev@example.com",
                "name", "Dev User"
            )
        );

        OidcUserInfo userInfo = new OidcUserInfo(Map.of(
            "sub", "user123",
            "email", "dev@example.com",
            "name", "Dev User"
        ));

        return new DefaultOidcUser(
            List.of(new SimpleGrantedAuthority("ROLE_USER")),
            idToken,
            userInfo
        );
    }
}
