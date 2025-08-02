package de.petunia.villadiana.config;

import de.petunia.villadiana.PetuniaSpecies;
import de.petunia.villadiana.axillaris.AxillarisGateway;
import de.petunia.villadiana.axillaris.AxillarisWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class AxillarisConfig {

   @Value("${app.client.axillaris-url}")
   private String axillarisUrl;

   @Bean
   @Profile({"prod", "int"})
   public WebClient axillarisClient(ClientRegistrationRepository clientRegistrationRepository,
                                    OAuth2AuthorizedClientRepository authorizedClientRepository) {
      var oauth2Filter = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
            clientRegistrationRepository,
            authorizedClientRepository
        );

      oauth2Filter.setDefaultClientRegistrationId("axillaris-client");

      return WebClient.builder()
              .baseUrl(axillarisUrl)
              .apply(oauth2Filter.oauth2Configuration())
            .build();
   }

   @Bean
   @Profile({"prod", "int"})
   public AxillarisGateway axillarisGateway(WebClient axillarisClient) {
      return new AxillarisWebClient(axillarisClient);
   }

   @Bean
   @Profile({"dev", "test"})
   public AxillarisGateway axillarisGatewayMock() {
      return new AxillarisGateway() {
         @Override
         public List<PetuniaSpecies> getAllPetunias() {
            return List.of(
                    new PetuniaSpecies("Petunia Axillaris", 32, ""),
                    new PetuniaSpecies("Petunia Villadiana", 30, "")
            );
         }
      };
   }
}
