package com.levanminh.car_rental.repository;

import com.levanminh.car_rental.entity.Car;
import com.levanminh.car_rental.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByStatus(CarStatus status);
    boolean existsByLicensePlate(String licensePlate);
} 