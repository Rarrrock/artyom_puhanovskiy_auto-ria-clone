package org.example.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.dto.AdResponse;
import org.example.dto.CarResponse;
import org.example.dto.FavoriteResponse;
import org.example.dto.UserResponse;
import org.example.entity.*;
import org.example.repository.AdRepository;
import org.example.repository.CarRepository;
import org.example.repository.FavoriteRepository;
import org.example.service.FavoriteService;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CarRepository carRepository;
    private final AdRepository adRepository;
    private final UserService userService;

    // Добавляю машину в избранное
    @Override
    public void addCarToFavorites(Long carId, String email) {
        User user = fetchUserEntityByEmail(email);
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + carId + " не найдена."));
        if (favoriteRepository.existsByUserAndCar(user.getId(), car)) {
            throw new IllegalArgumentException("Машина уже добавлена в избранное.");
        }
        Favorite favorite = new Favorite(null, user, car, null);
        favoriteRepository.save(favorite);
    }

    // Добавляю машину в избранное
    @Override
    public void addAdToFavorites(Long adId, String email) {
        User user = fetchUserEntityByEmail(email); // Использую сущность User
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + adId + " не найдено."));
        if (favoriteRepository.existsByUserAndAd(user.getId(), ad)) {
            throw new IllegalArgumentException("Объявление уже добавлено в избранное.");
        }
        Favorite favorite = new Favorite(null, user, null, ad);
        favoriteRepository.save(favorite);
    }

    @Override
    public List<CarResponse> getFavoriteCars(String email) {
        User user = fetchUserEntityByEmail(email); // Использую сущность User
        return favoriteRepository.findAllCarsByUser(user.getId())
                .stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdResponse> getFavoriteAds(String email) {
        User user = fetchUserEntityByEmail(email); // Использую сущность User
        return favoriteRepository.findAllAdsByUser(user.getId())
                .stream()
                .map(this::mapToAdResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CarResponse getFavoriteCarById(Long carId, String email) {
        User user = fetchUserEntityByEmail(email); // Использую сущность User
        Favorite favorite = favoriteRepository.findByUserAndCarId(user.getId(), carId)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + carId + " не найдена в избранном."));
        return mapToCarResponse(favorite.getCar());
    }

    @Override
    public AdResponse getFavoriteAdById(Long adId, String email) {
        User user = fetchUserEntityByEmail(email); // Использую сущность User
        Favorite favorite = favoriteRepository.findByUserAndAdId(user.getId(), adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + adId + " не найдено в избранном."));
        return mapToAdResponse(favorite.getAd());
    }

    @Override
    public void removeCarFromFavorites(Long carId, String email) {
        User user = fetchUserEntityByEmail(email); // Использую сущность User
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + carId + " не найдена."));
        Favorite favorite = favoriteRepository.findByUserAndCar(user.getId(), car)
                .orElseThrow(() -> new IllegalArgumentException("Машина не найдена в избранном."));
        favoriteRepository.delete(favorite);
    }

    @Override
    public void removeAdFromFavorites(Long adId, String email) {
        User user = fetchUserEntityByEmail(email); // Использую сущность User
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + adId + " не найдено."));
        Favorite favorite = favoriteRepository.findByUserAndAd(user.getId(), ad)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено в избранном."));
        favoriteRepository.delete(favorite);
    }

    // Преобразую UserResponse в User (для работы с репозиториями)
    private User fetchUserEntityByEmail(String email) {
        UserResponse userResponse = fetchUserResponseByEmail(email);
        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setRole(userResponse.getRole() != null ? RoleEnum.valueOf(userResponse.getRole()) : null);
        return user;
    }

    // Получаю UserResponse из UserService
    private UserResponse fetchUserResponseByEmail(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));
    }

    private CarResponse mapToCarResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .model(car.getModel())
                .enginePower(car.getEnginePower())
                .torque(car.getTorque())
                .ownerEmail(car.getOwner() != null ? car.getOwner().getEmail() : null)
                .lastMaintenanceTimestamp(car.getLastMaintenanceTimestamp())
                .build();
    }

    private AdResponse mapToAdResponse(Ad ad) {
        return AdResponse.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .price(ad.getPrice())
                .currency(ad.getCurrency())
                .status(ad.getStatus())
                .createdAt(ad.getCreatedAt())
                .updatedAt(ad.getUpdatedAt())
                .ownerEmail(ad.getOwner() != null ? ad.getOwner().getEmail() : null)
                .build();
    }
}