package org.example.repository;

import org.example.entity.Car;
import org.example.entity.Favorite;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserAndCar(User user, Car car);

    Optional<Favorite> findByUserAndCar(User user, Car car);

    @Query("SELECT f.car FROM Favorite f WHERE f.user = :user")
    List<Car> findAllCarsByUser(User user);
}