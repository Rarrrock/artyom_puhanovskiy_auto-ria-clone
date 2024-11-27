package org.example.service;

import org.example.entity.User;

import java.util.List;
import java.util.Optional;

// Основные методы работы с пользователями
public interface UserService {
    List<User> getAllUsers(); // Получаю всех пользователей

    User getUserById(Long id); // Получаю пользователя по ID

    User createUser(User user); // Создаю нового пользователя

    User updateUser(Long id, User user); // Обновляю данные существующего пользователя

    void deleteUser(Long id); // Удаляю пользователя по ID

    Optional<User> findByEmail(String email); // Ищу пользователя по email
}