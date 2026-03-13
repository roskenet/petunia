package de.roskenet.petunia.villadiana.dto;

import java.util.UUID;

public record PlaceOrderRequest(
        UUID playerId,
        String symbol,
        Long quantity,
        Long price,
        OrderSide side,
        OrderType type
) { }
