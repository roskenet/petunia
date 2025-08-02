package de.petunia.villadiana.axillaris;

import de.petunia.villadiana.PetuniaSpecies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class AxillarisWebClient implements AxillarisGateway {

    private final WebClient axillarisClient;

    public AxillarisWebClient(WebClient axillarisClient) {
        this.axillarisClient = axillarisClient;
    }

    @Override
    public List<PetuniaSpecies> getAllPetunias() {
        List<PetuniaSpecies> resultList = axillarisClient
                .get()
                .uri("/api/petunias")
                .retrieve()
                .bodyToFlux(PetuniaSpecies.class)
                .collectList().block();

        return resultList;
    }
}
