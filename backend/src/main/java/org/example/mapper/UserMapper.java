package org.example.mapper;

import org.example.dto.UserResponse;
import org.example.entity.User;

public class UserMapper {
    public static User mapToEntity(UserResponse userResponse) {
        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setRole(userResponse.getRole());
        user.setPassword(userResponse.getPassword());
        user.setAccountType(userResponse.getAccountType());
        return user;
    }

    public static UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .accountType(user.getAccountType())
                .build();
    }
}