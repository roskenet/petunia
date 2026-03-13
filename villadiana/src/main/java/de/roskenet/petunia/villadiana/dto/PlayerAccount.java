package de.roskenet.petunia.villadiana.dto;

import java.util.UUID;

public record PlayerAccount (
        UUID id,
        String playerName,
        int balance
    ) { }