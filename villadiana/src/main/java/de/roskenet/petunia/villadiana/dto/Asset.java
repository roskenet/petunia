package de.roskenet.petunia.villadiana.dto;

import java.util.UUID;

public record Asset(UUID id, UUID accountId, String symbol, long quantity) {
}