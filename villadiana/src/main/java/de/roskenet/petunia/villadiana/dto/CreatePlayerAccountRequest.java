package de.roskenet.petunia.villadiana.dto;

import java.util.UUID;

public record CreatePlayerAccountRequest(UUID playerSubject, String playerName) {
}
