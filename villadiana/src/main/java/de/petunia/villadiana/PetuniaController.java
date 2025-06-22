package de.petunia.villadiana;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Slf4j
public class PetuniaController {

    @GetMapping("/petunias")
    public PetuniaSpecies getPetuniaSpecies() {
        return new PetuniaSpecies("Petunia", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Petunia_in_Orangenburg.jpg/1200px-Petunia_in_Orangenburg.jpg");
    }

    @PostMapping("/petunias")
    public PetuniaSpecies postPetuniaSpecies(@RequestBody PetuniaSpecies species) {
        log.info("Received species: {}", species);
        return species;
    }
}
