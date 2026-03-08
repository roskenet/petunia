package de.roskenet.petunia.villadiana;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class PetuniaController {
    @GetMapping("/petunias")
    @PreAuthorize("hasAnyRole('trader', 'admin')")
    public List<PetuniaSpecies> getPetuniaSpecies(@AuthenticationPrincipal OidcUser principal) {
        System.out.println(principal);
        return Arrays.asList(
                new PetuniaSpecies("Petunia", 1, "https://example.com/petunia.jpg")
        );
    }

    @PostMapping("/petunias")
    public PetuniaSpecies postPetuniaSpecies(@RequestBody PetuniaSpecies species) {
//        log.info("Received species: {}", species);
        System.out.println("Received species: " + species);
        return species;
    }
}
