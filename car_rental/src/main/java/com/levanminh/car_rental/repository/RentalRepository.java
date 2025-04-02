package com.levanminh.car_rental.repository;

import com.levanminh.car_rental.entity.Rental;
import com.levanminh.car_rental.entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByCustomerId(Long customerId);
    List<Rental> findByCarId(Long carId);
    List<Rental> findByStatus(RentalStatus status);
    boolean existsByCarIdAndStatus(Long carId, RentalStatus status);
} 