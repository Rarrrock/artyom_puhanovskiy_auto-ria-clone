package org.example.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.dto.AdResponse;
import org.example.dto.CarResponse;
import org.example.dto.UserResponse;
import org.example.entity.*;
import org.example.enums.RoleEnum;
import org.example.mapper.AdMapper;
import org.example.mapper.CarMapper;
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

    // TODO: Подумать когда использовать
    // Проверяю права пользователя
    private void checkUserAccess(User user, String email) {
        if (!user.getEmail().equals(email) && !userService.isAdminOrManager(email)) {
            throw new SecurityException("У вас нет прав для выполнения этой операции.");
        }
    }

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

    // Добавляю объявление в избранное
    @Override
    public void addAdToFavorites(Long adId, String email) {
        User user = fetchUserEntityByEmail(email);
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + adId + " не найдено."));
        if (favoriteRepository.existsByUserAndAd(user.getId(), ad)) {
            throw new IllegalArgumentException("Объявление уже добавлено в избранное.");
        }
        Favorite favorite = new Favorite(null, user, null, ad);
        favoriteRepository.save(favorite);
    }

    // Получаю машины из избранного
    @Override
    public List<CarResponse> getFavoriteCars(String email) {
        User user = fetchUserEntityByEmail(email);
        return favoriteRepository.findAllCarsByUser(user.getId())
                .stream()
                .map(CarMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    // Получаю объявления из избранного
    @Override
    public List<AdResponse> getFavoriteAds(String email) {
        User user = fetchUserEntityByEmail(email);
        return favoriteRepository.findAllAdsByUser(user.getId())
                .stream()
                .map(AdMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    // Получаю машину из избранного по ID
    @Override
    public CarResponse getFavoriteCarById(Long carId, String email) {
        User user = fetchUserEntityByEmail(email);
        Favorite favorite = favoriteRepository.findByUserAndCarId(user.getId(), carId)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + carId + " не найдена в избранном."));
        return CarMapper.mapToResponse(favorite.getCar());
    }

    // Получаю объявление из избранного по ID
    @Override
    public AdResponse getFavoriteAdById(Long adId, String email) {
        User user = fetchUserEntityByEmail(email);
        Favorite favorite = favoriteRepository.findByUserAndAdId(user.getId(), adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + adId + " не найдено в избранном."));
        return AdMapper.mapToResponse(favorite.getAd());
    }

    // Удаляю машину из избранного по ID
    @Override
    public void removeCarFromFavorites(Long carId, String email) {
        User user = fetchUserEntityByEmail(email);
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Машина с ID " + carId + " не найдена."));
        Favorite favorite = favoriteRepository.findByUserAndCar(user.getId(), car)
                .orElseThrow(() -> new IllegalArgumentException("Машина не найдена в избранном."));
        if (!user.getEmail().equals(email) && !userService.isAdminOrManager(email)) {
            throw new IllegalArgumentException("У вас нет прав для удаления этой машины из избранного.");
        }
        favoriteRepository.delete(favorite);
    }

    // Удаляю объявление из избранного по ID
    @Override
    public void removeAdFromFavorites(Long adId, String email) {
        User user = fetchUserEntityByEmail(email);
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + adId + " не найдено."));
        Favorite favorite = favoriteRepository.findByUserAndAd(user.getId(), ad)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено в избранном."));
        if (!user.getEmail().equals(email) && !userService.isAdminOrManager(email)) {
            throw new IllegalArgumentException("У вас нет прав для удаления этого объявления из избранного.");
        }
        favoriteRepository.delete(favorite);
    }

    // TODO: Вынести логику в отдельный класс
    // Преобразую UserResponse в User (для работы с репозиториями)
    private User fetchUserEntityByEmail(String email) {
        UserResponse userResponse = fetchUserResponseByEmail(email);
        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setRole(userResponse.getRole());
        return user;
    }

    // TODO: Вынести логику в отдельный класс
    // Получаю UserResponse из UserService
    private UserResponse fetchUserResponseByEmail(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));
    }
}