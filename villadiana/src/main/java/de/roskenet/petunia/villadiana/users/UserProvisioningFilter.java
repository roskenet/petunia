package de.roskenet.petunia.villadiana.users;

import de.roskenet.petunia.dto.CreatePlayerAccountRequest;
import de.roskenet.petunia.villadiana.routes.admin.AdminPlayersController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
/*
Makes sure that a self signed user gets created in our system
 */
@Component
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
            DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            OidcUserInfo userInfo = principal.getUserInfo();
            UUID keycloakId = UUID.fromString(userInfo.getClaimAsString("sub"));
            String name = userInfo.getClaimAsString("name");
            adminPlayersController.createPlayer(new CreatePlayerAccountRequest(keycloakId, name));
        }

        filterChain.doFilter(request, response);
    }
}
