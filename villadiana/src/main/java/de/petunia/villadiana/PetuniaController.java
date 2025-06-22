package de.petunia.villadiana;

import lombok.extern.slf4j.Slf4j;
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
    public List<PetuniaSpecies> getPetuniaSpecies() {
        return Arrays.asList(
                new PetuniaSpecies(
                        "Petunia Alpicola",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Petunia_in_Orangenburg.jpg/1200px-Petunia_in_Orangenburg.jpg"),
                new PetuniaSpecies(
                        "Petunia Villadiana",
                        ""
                )
        );
    }

    @PostMapping("/petunias")
    public PetuniaSpecies postPetuniaSpecies(@RequestBody PetuniaSpecies species) {
        log.info("Received species: {}", species);
        return species;
    }
}
