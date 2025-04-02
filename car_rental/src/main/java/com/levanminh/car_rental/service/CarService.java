package com.levanminh.car_rental.service;

import com.levanminh.car_rental.entity.Car;
import com.levanminh.car_rental.entity.CarStatus;
import com.levanminh.car_rental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
    }

    public List<Car> getCarsByStatus(CarStatus status) {
        return carRepository.findByStatus(status);
    }

    @Transactional
    public Car createCar(Car car) {
        if (carRepository.existsByLicensePlate(car.getLicensePlate())) {
            throw new RuntimeException("License plate already exists");
        }
        car.setStatus(CarStatus.AVAILABLE);
        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, Car car) {
        Car existingCar = getCarById(id);
        existingCar.setBrand(car.getBrand());
        existingCar.setModel(car.getModel());
        existingCar.setPricePerDay(car.getPricePerDay());
        existingCar.setDescription(car.getDescription());
        return carRepository.save(existingCar);
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = getCarById(id);
        if (car.getStatus() == CarStatus.RENTED) {
            throw new RuntimeException("Cannot delete rented car");
        }
        carRepository.deleteById(id);
    }

    @Transactional
    public Car updateCarStatus(Long id, CarStatus status) {
        Car car = getCarById(id);
        car.setStatus(status);
        return carRepository.save(car);
    }
} 