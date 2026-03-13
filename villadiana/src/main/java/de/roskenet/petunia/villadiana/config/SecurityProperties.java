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
    public List<String> allowedOriginPatterns;
    public List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    public List<String> allowedHeaders = List.of("*");
    public boolean allowCredentials = true;
    public String postLogoutRedirectUri;
    public String defaultLoginSuccessUrl = "/";
    public String resourceAccessClientId = "";
    public String loginUri = "/login";
    public String logoutUri = "/logout";
}
