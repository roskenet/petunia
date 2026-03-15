package de.roskenet.petunia.villadiana.bff;

import java.util.List;
import java.util.UUID;

public record UserInfo(
    String name,
    String email,
    UUID sub,
    List<String> roles
) { }

