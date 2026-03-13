package de.roskenet.petunia.villadiana.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID playerId,
        String symbol,
        long quantity,
        long remainingQuantity,
        long price,
        OrderSide side,
        OrderType type,
        OffsetDateTime createdAt
) { }
