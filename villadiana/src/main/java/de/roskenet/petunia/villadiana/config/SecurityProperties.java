package de.roskenet.petunia.villadiana.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class SecurityProperties {
    private List<String> allowedOriginPatterns;
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private List<String> allowedHeaders = List.of("*");
    private boolean allowCredentials = true;
    private String postLogoutRedirectUri;
    private String defaultLoginSuccessUrl = "/";
    private String resourceAccessClientId = "";
    private String loginUri = "/login";
    private String logoutUri = "/logout";
}
