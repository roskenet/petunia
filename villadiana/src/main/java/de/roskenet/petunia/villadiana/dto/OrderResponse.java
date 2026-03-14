package de.roskenet.petunia.villadiana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        @JsonProperty("player_id")
        UUID playerId,
        String symbol,
        long quantity,
        long price,
        OrderSide side,
        OrderType type,
        OffsetDateTime createdAt
) { }
