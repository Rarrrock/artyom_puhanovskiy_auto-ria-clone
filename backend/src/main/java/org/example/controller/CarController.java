package org.example.controller;

import org.example.dto.CarRequest;
import org.example.entity.Car;
import org.example.entity.User;
import org.example.service.CarService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService; // Добавляю поле для работы с UserService

    // Получаю список всех машин
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    // Добавляю новую машину
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody CarRequest carRequest) {
        // Ищу владельца по ID
        User owner = null;
        if (carRequest.getOwnerId() != null) {
            owner = userService.getUserById(carRequest.getOwnerId()); // Теперь userService доступен
        }

        // Создаю объект машины
        Car car = new Car();
        car.setModel(carRequest.getModel());
        car.setEnginePower(carRequest.getEnginePower());
        car.setTorque(carRequest.getTorque());
        car.setOwner(owner);
        car.setLastMaintenanceTimestamp(carRequest.getLastMaintenanceTimestamp());

        // Сохраняю машину через сервис
        Car createdCar = carService.createCar(car);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    // Получаю машину по ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    // Обновляю данные машины
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        Car updatedCar = carService.updateCar(id, carDetails);
        return ResponseEntity.ok(updatedCar);
    }

    // Удаляю машину по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    // Добавляю фильтрацию машин по параметрам
    @GetMapping("/filter")
    public ResponseEntity<List<Car>> filterCars(
            @RequestParam(required = false) Integer minEnginePower,
            @RequestParam(required = false) Integer maxEnginePower,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long minTimestamp,
            @RequestParam(required = false) Long maxTimestamp) {

        // Фильтрую машины через сервис
        List<Car> filteredCars = carService.filterCars(minEnginePower, maxEnginePower, ownerId, minTimestamp, maxTimestamp);
        return ResponseEntity.ok(filteredCars);
    }
}