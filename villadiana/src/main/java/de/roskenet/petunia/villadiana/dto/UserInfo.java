package de.roskenet.petunia.villadiana.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserInfo {
    private String name;
    private String email;
    private UUID sub;
    private List<String> roles;
}


