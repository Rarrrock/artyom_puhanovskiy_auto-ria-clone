package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.UserResponse;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest request) {
        // Проверяю наличие пользователя
        Optional<User> existingUser = userService.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email уже используется!");
        }

        // Создаю нового пользователя
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userService.createUser(user); // Сохраняю пользователя через UserService
    }

    public String login(LoginRequest request) {
        // Ищу пользователя
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден!"));

        // Проверяю пароль
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Неверный email или пароль!");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    public UserResponse getCurrentUser(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден!"));

        // Собираю ответ
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }

    @PostConstruct
    public void updateAdminPassword() {
        // Ищу пользователя admin@example.com
        User user = userService.findByEmail("admin@example.com")
                .orElseThrow(() -> new IllegalArgumentException("Пользователь admin@example.com не найден!"));

        // Шифрую пароль и сохраняю
        user.setPassword(passwordEncoder.encode("admin123"));
        userService.createUser(user);

        // Лог для проверки
        System.out.println("Пароль администратора обновлён.");
    }
}