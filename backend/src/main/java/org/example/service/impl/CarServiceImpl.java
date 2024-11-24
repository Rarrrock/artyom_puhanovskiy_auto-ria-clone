package org.example.service.impl;

import org.example.entity.Car;
import org.example.repository.CarRepository;
import org.example.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    // Получаю список всех машин
    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Добавляю новую машину
    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    // Получаю машину по ID
    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Машина с ID " + id + " не найдена."));
    }

    // Обновляю данные машины
    @Override
    public Car updateCar(Long id, Car carDetails) {
        Car car = getCarById(id); // Проверяю, что машина существует
        car.setModel(carDetails.getModel());
        car.setEnginePower(carDetails.getEnginePower());
        car.setTorque(carDetails.getTorque());
        car.setLastMaintenanceTimestamp(carDetails.getLastMaintenanceTimestamp());
        return carRepository.save(car);
    }

    // Удаляю машину по ID
    @Override
    public void deleteCar(Long id) {
        Car car = getCarById(id); // Проверяю, что машина существует
        carRepository.delete(car);
    }

    @Override
    public List<Car> filterCars(Integer minEnginePower, Integer maxEnginePower, Long ownerId, Long minTimestamp, Long maxTimestamp) {
        // Выполняю запрос с фильтрацией через репозиторий
        return carRepository.findFilteredCars(minEnginePower, maxEnginePower, ownerId, minTimestamp, maxTimestamp);
    }
}