package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Car;
import org.example.entity.User;
import org.example.service.CarService;
import org.example.service.FavoriteService;
import org.example.service.UserService;
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

    @PostMapping("/{carId}")
    public String addFavorite(@PathVariable Long carId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Car car = carService.getCarById(carId);
        favoriteService.addFavorite(user, car);
        return "Машина добавлена в избранное";
    }

    @DeleteMapping("/{carId}")
    public String removeFavorite(@PathVariable Long carId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Car car = carService.getCarById(carId);
        favoriteService.removeFavorite(user, car);
        return "Машина удалена из избранного";
    }

    @GetMapping
    public List<Car> getFavorites(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        return favoriteService.getFavorites(user);
    }
}