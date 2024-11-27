package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Car;
import org.example.entity.User;
import org.example.service.CarService;
import org.example.service.FavoriteService;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final CarService carService;
    private final UserService userService;

    // Добавляю в Избранное
    @PostMapping("/{carId}")
    public ResponseEntity<String> addFavorite(@PathVariable Long carId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));
        Car car = carService.getCarById(carId);
        favoriteService.addFavorite(user, car);
        return ResponseEntity.status(HttpStatus.CREATED).body("Машина добавлена в избранное.");
    }

    // Удаляю из Избранного
    @DeleteMapping("/{carId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long carId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));
        Car car = carService.getCarById(carId);
        favoriteService.removeFavorite(user, car);
        return ResponseEntity.ok("Машина удалена из избранного.");
    }

    // Получаю Избранное
    @GetMapping
    public ResponseEntity<List<Car>> getFavorites(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));
        List<Car> favorites = favoriteService.getFavorites(user);
        return ResponseEntity.ok(favorites);
    }
}