package de.roskenet.petunia.villadiana.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfo {
    private String name;
    private String email;
    private List<String> roles;
}


