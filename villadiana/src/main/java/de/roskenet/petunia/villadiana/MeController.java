package de.roskenet.petunia.villadiana;

import de.roskenet.petunia.villadiana.config.SecurityProperties;
import de.roskenet.petunia.villadiana.dto.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final SecurityProperties securityProperties;

    public MeController(
            SecurityProperties securityProperties
    ) {
        this.securityProperties = securityProperties;
    }

    @GetMapping
    public UserInfo getMe(@AuthenticationPrincipal OidcUser user) {
        String name = user.getFullName();
        String email = user.getEmail();
        UUID sub = UUID.fromString(user.getSubject());
        List<String> roles = extractRoles(user);

        return new UserInfo(name, email, sub, roles);
    }

    private List<String> extractRoles(OidcUser user) {
        Map<String, Object> resourceAccess = user.getAttribute("resource_access");

        if (resourceAccess != null) {
            Map<String, Object> client = (Map<String, Object>) resourceAccess.get(securityProperties.getResourceAccessClientId());

            if (client != null) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) client.get("roles");

                if (roles != null) {
                    return roles;
                }
            }
        }

        return Collections.emptyList();
    }
}
