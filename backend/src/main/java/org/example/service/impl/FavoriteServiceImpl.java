package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Car;
import org.example.entity.Favorite;
import org.example.entity.User;
import org.example.repository.FavoriteRepository;
import org.example.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Override
    public void addFavorite(User user, Car car) {
        if (favoriteRepository.existsByUserAndCar(user, car)) {
            throw new IllegalArgumentException("Машина уже добавлена в избранное.");
        }
        Favorite favorite = new Favorite(null, user, car);
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavorite(User user, Car car) {
        Favorite favorite = favoriteRepository.findByUserAndCar(user, car)
                .orElseThrow(() -> new IllegalArgumentException("Машина не найдена в избранном."));
        favoriteRepository.delete(favorite);
    }

    @Override
    public List<Car> getFavorites(User user) {
        return favoriteRepository.findAllCarsByUser(user);
    }
}