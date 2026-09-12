package de.roskenet.petunia.villadiana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record PlayerAccount (
        UUID id,
        @JsonProperty("player_name")
        String playerName,
        int balance
    ) { }