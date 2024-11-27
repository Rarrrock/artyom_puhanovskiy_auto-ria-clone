package org.example.service;

import org.example.entity.Car;
import org.example.entity.Favorite;
import org.example.entity.User;

import java.util.List;

public interface FavoriteService {
    void addFavorite(User user, Car car);
    void removeFavorite(User user, Car car);
    List<Car> getFavorites(User user);
}