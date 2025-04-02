package com.levanminh.car_rental.synchronizer;

import com.levanminh.car_rental.entity.Customer;
import com.levanminh.car_rental.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSynchronizer {
    private final CustomerRepository customerRepository;

    public void synchronizeWithIDP(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        String preferredUsername = jwt.getClaimAsString("preferred_username");

        if (email != null && !email.isEmpty()) {
            customerRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Customer customer = new Customer();
                        customer.setEmail(email);
                        customer.setName(name != null ? name : preferredUsername);
                        customer.setPhone(preferredUsername); // Sử dụng preferred_username làm số điện thoại tạm thời
                        customer.setAddress("Chưa cập nhật"); // Địa chỉ mặc định
                        customer.setDateOfBirth(LocalDate.now()); // Ngày sinh mặc định
                        customer.setIdentityCard("Chưa cập nhật"); // CMND mặc định
                        return customerRepository.save(customer);
                    });
        }
    }
} 