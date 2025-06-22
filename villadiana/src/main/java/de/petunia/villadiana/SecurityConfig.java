package de.petunia.villadiana;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("prod")
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("http://localhost:3000", "http://alpicola")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().permitAll())
//                .oauth2Login(oauth -> oauth
//                         .loginPage("/oauth2/authorization/keycloak"))   // Einstiegspunkt fürs SPA
//                .logout(logout -> logout
//                        .logoutSuccessHandler(oidcLogoutSuccessHandler()))
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                )
                .oauth2Login(Customizer.withDefaults()) // Nur wenn du Login UI brauchst
//                .oauth2Login(oauth -> oauth
//                         .loginPage("/oauth2/authorization/keycloak"))   // Einstiegspunkt fürs SPA
//            .csrf(AbstractHttpConfigurer::disable) // Vorsicht bei echten POSTs!
//            .sessionManagement(sm -> sm
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//            )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            request.getSession().invalidate();
                            response.setStatus(HttpServletResponse.SC_OK);
                        }).permitAll()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((req, res, authException) -> {
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthenticated");
                        })
                );
//            .cors(Customizer.withDefaults());

        return http.build();
    }

//    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
//        OidcClientInitiatedLogoutSuccessHandler handler =
//                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
//        handler.setPostLogoutRedirectUri("{baseUrl}/");
//        return handler;
//    }
}
