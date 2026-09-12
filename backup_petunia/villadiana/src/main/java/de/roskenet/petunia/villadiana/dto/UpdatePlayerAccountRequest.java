package de.roskenet.petunia.villadiana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UpdatePlayerAccountRequest(
        @JsonProperty("player_id")
        UUID playerId,
        @JsonProperty("player_name")
        String playerName,
        Long balance) { }
