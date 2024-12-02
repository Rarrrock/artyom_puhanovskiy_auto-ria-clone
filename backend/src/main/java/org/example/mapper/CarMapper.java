package org.example.mapper;

import org.example.dto.CarResponse;
import org.example.entity.Car;
import org.example.dto.CarRequest;

public class CarMapper {
    public static Car mapToEntity(CarRequest carRequest) {
        return Car.builder()
                .model(carRequest.getModel())
                .enginePower(carRequest.getEnginePower())
                .torque(carRequest.getTorque())
                .lastMaintenanceTimestamp(carRequest.getLastMaintenanceTimestamp())
                .build();
    }

    public static CarResponse mapToResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .model(car.getModel())
                .enginePower(car.getEnginePower())
                .torque(car.getTorque())
                .ownerEmail(car.getOwner() != null ? car.getOwner().getEmail() : null)
                .lastMaintenanceTimestamp(car.getLastMaintenanceTimestamp())
                .build();
    }
}