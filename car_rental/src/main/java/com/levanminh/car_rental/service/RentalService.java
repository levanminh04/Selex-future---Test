package com.levanminh.car_rental.service;

import com.levanminh.car_rental.entity.Car;
import com.levanminh.car_rental.entity.CarStatus;
import com.levanminh.car_rental.entity.Customer;
import com.levanminh.car_rental.entity.Rental;
import com.levanminh.car_rental.entity.RentalStatus;
import com.levanminh.car_rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;
    private final CustomerService customerService;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));
    }

    public List<Rental> getRentalsByCustomerId(Long customerId) {
        return rentalRepository.findByCustomerId(customerId);
    }

    public List<Rental> getRentalsByCarId(Long carId) {
        return rentalRepository.findByCarId(carId);
    }

    @Transactional
    public Rental createRental(Long carId, Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        Car car = carService.getCarById(carId);
        Customer customer = customerService.getCustomerById(customerId);

        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new RuntimeException("Car is not available");
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Start date must be before end date");
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days < 1) {
            throw new RuntimeException("Rental period must be at least 1 day");
        }

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalPrice(car.getPricePerDay().multiply(java.math.BigDecimal.valueOf(days)));
        rental.setStatus(RentalStatus.PENDING);

        carService.updateCarStatus(carId, CarStatus.RENTED);
        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental updateRentalStatus(Long id, RentalStatus status) {
        Rental rental = getRentalById(id);
        rental.setStatus(status);
        
        if (status == RentalStatus.COMPLETED || status == RentalStatus.CANCELLED) {
            carService.updateCarStatus(rental.getCar().getId(), CarStatus.AVAILABLE);
        }
        
        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental changeCar(Long rentalId, Long newCarId) {
        Rental rental = getRentalById(rentalId);
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new RuntimeException("Can only change car for active rentals");
        }

        Car newCar = carService.getCarById(newCarId);
        if (newCar.getStatus() != CarStatus.AVAILABLE) {
            throw new RuntimeException("New car is not available");
        }

        // Calculate remaining days and price
        long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), rental.getEndDate());
        if (remainingDays < 1) {
            throw new RuntimeException("Cannot change car for rentals ending soon");
        }

        // Update old car status
        carService.updateCarStatus(rental.getCar().getId(), CarStatus.AVAILABLE);
        
        // Update rental with new car
        rental.setCar(newCar);
        rental.setTotalPrice(newCar.getPricePerDay().multiply(java.math.BigDecimal.valueOf(remainingDays)));
        rental.setStatus(RentalStatus.CHANGED);
        
        // Update new car status
        carService.updateCarStatus(newCarId, CarStatus.RENTED);
        
        return rentalRepository.save(rental);
    }

    @Transactional
    public void deleteRental(Long id) {
        Rental rental = getRentalById(id);
        if (rental.getStatus() == RentalStatus.ACTIVE) {
            throw new RuntimeException("Cannot delete active rental");
        }
        rentalRepository.deleteById(id);
    }
} 