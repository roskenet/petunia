package de.roskenet.petunia.villadiana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CreatePlayerAccountRequest(
        @JsonProperty("player_subject")
        UUID playerSubject,
        @JsonProperty("player_name")
        String playerName) {
}
