package de.roskenet.petunia.villadiana;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PetuniaSpecies(
        String name,
        @JsonProperty("petal_length")
        Integer petalLength,
        String imageUrl) {
}
