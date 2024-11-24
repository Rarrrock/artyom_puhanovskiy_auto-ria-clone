package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Генерирую конструктор с финальными полями
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        // Возвращаю всех пользователей
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        // Ищу пользователя по ID
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден."));
    }

    @Override
    public User createUser(User user) {
        // Сохраняю нового пользователя
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        // Обновляю данные пользователя
        User existingUser = getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        // Удаляю пользователя по ID
        userRepository.deleteById(id);
    }
}