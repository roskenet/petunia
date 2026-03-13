package de.roskenet.petunia.villadiana.users;

import de.roskenet.petunia.villadiana.dto.CreatePlayerAccountRequest;
import de.roskenet.petunia.villadiana.dto.PlayerAccount;
import de.roskenet.petunia.villadiana.routes.admin.AdminPlayersController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
/*
Makes sure that a self signed user gets created in our system
 */
@Component
@Profile({"prod", "int", "local"})
public class UserProvisioningFilter extends OncePerRequestFilter {

    private final AdminPlayersController adminPlayersController;

    public UserProvisioningFilter(AdminPlayersController adminPlayersController) {
        this.adminPlayersController = adminPlayersController;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth != null) {
            DefaultOidcUser principal = (DefaultOidcUser) auth.getPrincipal();

            if (forcePlayerCreation(principal)) {
                OidcUserInfo userInfo = principal.getUserInfo();
                UUID keycloakId = UUID.fromString(userInfo.getClaimAsString("sub"));
                String name = userInfo.getClaimAsString("name");

                adminPlayersController.createPlayer(new CreatePlayerAccountRequest(keycloakId, name));
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean forcePlayerCreation(DefaultOidcUser principal) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) principal.getAuthorities();

        boolean isTrader = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_trader"));

        if (!isTrader) {
            return false;
        }

        UUID keycloakId = UUID.fromString(principal.getUserInfo().getClaimAsString("sub"));
        PlayerAccount player = adminPlayersController.getPlayer(keycloakId);

        return player == null;
    }
}
