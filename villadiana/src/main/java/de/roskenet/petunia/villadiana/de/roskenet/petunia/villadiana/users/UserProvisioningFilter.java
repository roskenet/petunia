package de.roskenet.petunia.villadiana.de.roskenet.petunia.villadiana.users;

import de.roskenet.petunia.villadiana.routes.admin.AdminPlayersController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {

            String keycloakId = jwt.getSubject();
            String username = jwt.getClaimAsString("preferred_username");

//            userService.ensureUserExists(keycloakId, username);
        }

        filterChain.doFilter(request, response);
    }
}
