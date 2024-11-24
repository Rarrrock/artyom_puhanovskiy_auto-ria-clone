package org.example.service;

import org.example.entity.Car;

import java.util.List;

// Основные методы работы с машинами
public interface CarService {
    List<Car> getAllCars(); // Получаю список всех машин
    Car createCar(Car car); // Добавляю новую машину
    Car getCarById(Long id); // Получаю машину по ID
    Car updateCar(Long id, Car carDetails); // Обновляю данные машины
    void deleteCar(Long id); // Удаляю машину по ID

    // Фильтрую машины по заданным параметрам
    List<Car> filterCars(Integer minEnginePower, Integer maxEnginePower, Long ownerId, Long minTimestamp, Long maxTimestamp);
}
