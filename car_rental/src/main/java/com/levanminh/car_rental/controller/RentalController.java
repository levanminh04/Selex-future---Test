package com.levanminh.car_rental.controller;

import com.levanminh.car_rental.entity.Rental;
import com.levanminh.car_rental.entity.RentalStatus;
import com.levanminh.car_rental.service.RentalService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rental")
@SecurityRequirement(name = "keycloak")
public class RentalController {
    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(
            @Parameter(description = "ID của hợp đồng thuê") @PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Rental>> getRentalsByCustomerId(
            @Parameter(description = "ID của khách hàng") @PathVariable Long customerId) {
        return ResponseEntity.ok(rentalService.getRentalsByCustomerId(customerId));
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Rental>> getRentalsByCarId(
            @Parameter(description = "ID của xe") @PathVariable Long carId) {
        return ResponseEntity.ok(rentalService.getRentalsByCarId(carId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Rental> createRental(
            @Parameter(description = "ID của xe") @RequestParam Long carId,
            @Parameter(description = "ID của khách hàng") @RequestParam Long customerId,
            @Parameter(description = "Ngày giờ bắt đầu thuê") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Ngày giờ kết thúc thuê") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(rentalService.createRental(carId, customerId, startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateRentalStatus(
            @Parameter(description = "ID của hợp đồng thuê") @PathVariable Long id,
            @Parameter(description = "Trạng thái mới của hợp đồng") @RequestParam RentalStatus status) {
        rentalService.updateRentalStatus(id, status);
    }

    @PatchMapping("/{id}/change-car")
    @ResponseStatus(HttpStatus.OK)
    public void changeCar(
            @Parameter(description = "ID của hợp đồng thuê") @PathVariable Long id,
            @Parameter(description = "ID của xe mới") @RequestParam Long newCarId) {
        rentalService.changeCar(id, newCarId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRental(
            @Parameter(description = "ID của hợp đồng thuê") @PathVariable Long id) {
        rentalService.deleteRental(id);
    }
} 