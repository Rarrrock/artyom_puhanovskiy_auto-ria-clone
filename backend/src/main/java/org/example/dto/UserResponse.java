package org.example.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String email;
    private String role;
}
