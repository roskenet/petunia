package de.roskenet.petunia.villadiana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record PlaceOrderRequest(
        @JsonProperty("player_id")
        UUID playerId,
        String symbol,
        Long quantity,
        Long price,
        OrderSide side,
        OrderType type
) { }
