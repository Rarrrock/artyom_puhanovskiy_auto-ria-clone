package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.entity.RoleEnum;

@Data
@Builder
public class UserRequest {
    private RoleEnum role;
    private String username;
    private String email;
    private String password;
}