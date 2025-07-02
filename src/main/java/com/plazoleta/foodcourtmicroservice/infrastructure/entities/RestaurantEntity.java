package com.plazoleta.foodcourtmicroservice.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String nit;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String urlLogo;

    @Column
    private Long ownerId;
}
