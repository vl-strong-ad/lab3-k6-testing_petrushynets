package com.example.rgr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    // Зв'язок: Багато товарів належать одному магазину
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
}