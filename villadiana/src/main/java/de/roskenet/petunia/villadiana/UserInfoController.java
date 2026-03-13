package de.roskenet.petunia.villadiana;

import de.roskenet.petunia.villadiana.config.SecurityProperties;
import de.roskenet.petunia.villadiana.dto.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class UserInfoController {

    private final SecurityProperties securityProperties;

    public UserInfoController(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @GetMapping("/me")
    public UserInfo me(@AuthenticationPrincipal OidcUser user) {
        String name = user.getFullName();
        String email = user.getEmail();
        List<String> roles = extractRoles(user);

        return new UserInfo(name, email, roles);
    }

    private List<String> extractRoles(OidcUser user) {
        Map<String, Object> resourceAccess = user.getAttribute("resource_access");

        if (resourceAccess != null) {
            Map<String, Object> client = (Map<String, Object>) resourceAccess.get(securityProperties.getResourceAccessClientId());

            if (client != null) {
                List<String> roles = (List<String>) client.get("roles");

                if (roles != null) {
                    return roles;
                }
            }
        }

        return Collections.emptyList();
    }

}
