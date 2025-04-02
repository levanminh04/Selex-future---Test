package com.levanminh.car_rental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String firstName;
    private String lastName;
} 