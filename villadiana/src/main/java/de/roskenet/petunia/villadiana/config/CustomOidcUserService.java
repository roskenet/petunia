package de.roskenet.petunia.villadiana.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());

        Map<String, Object> resourceAccess = oidcUser.getAttribute("resource_access");

        if (resourceAccess != null) {
            Map<String, Object> client = (Map<String, Object>) resourceAccess.get("villadiana");

            if (client != null) {
                List<String> roles = (List<String>) client.get("roles");

                if (roles != null) {
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                }
            }
        }

        return new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "preferred_username"
        );
    }
}