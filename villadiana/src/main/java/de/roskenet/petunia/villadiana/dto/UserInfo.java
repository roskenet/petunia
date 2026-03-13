package de.roskenet.petunia.villadiana.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserInfo {
    private String name;
    private String email;
    private UUID sub;
    private List<String> roles;

    public UserInfo(String name, String email, UUID sub, List<String> roles) {
        this.name = name;
        this.email = email;
        this.sub = sub;
        this.roles = roles;
    }
}


