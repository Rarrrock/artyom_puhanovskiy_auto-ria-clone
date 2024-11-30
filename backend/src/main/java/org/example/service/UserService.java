package org.example.service;

import org.example.dto.UserRequest;
import org.example.dto.UserResponse;

import java.util.List;
import java.util.Optional;

// Интерфейс для работы с пользователями
public interface UserService {
    List<UserResponse> getAllUsers(); // Получаю всех пользователей

    UserResponse getUserById(Long id); // Получаю пользователя по ID

    UserResponse createUser(UserRequest userRequest); // Создаю нового пользователя

    UserResponse updateUser(Long id, UserRequest userRequest); // Обновляю данные существующего пользователя

    void deleteUser(Long id); // Удаляю пользователя по ID

    Optional<UserResponse> findByEmail(String email); // Ищу пользователя по email
}