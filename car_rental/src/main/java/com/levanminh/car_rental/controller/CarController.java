package com.levanminh.car_rental.controller;

import com.levanminh.car_rental.entity.Car;
import com.levanminh.car_rental.entity.CarStatus;
import com.levanminh.car_rental.service.CarService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Tag(name = "Car")
@SecurityRequirement(name = "keycloak")
public class CarController {
    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(
            @Parameter(description = "ID của xe") @PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Car>> getCarsByStatus(
            @Parameter(description = "Trạng thái của xe") @PathVariable CarStatus status) {
        return ResponseEntity.ok(carService.getCarsByStatus(status));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.createCar(car));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCar(
            @Parameter(description = "ID của xe") @PathVariable Long id,
            @RequestBody Car car) {
        carService.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(
            @Parameter(description = "ID của xe") @PathVariable Long id) {
        carService.deleteCar(id);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateCarStatus(
            @Parameter(description = "ID của xe") @PathVariable Long id,
            @Parameter(description = "Trạng thái mới của xe") @RequestParam CarStatus status) {
        carService.updateCarStatus(id, status);
    }
} 