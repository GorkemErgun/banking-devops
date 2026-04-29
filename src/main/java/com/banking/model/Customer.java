package com.banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(max = 16, message = "Name must be at most 16 characters")
    @Column(length = 16, nullable = false)
    private String name;

    @Size(max = 32, message = "Address must be at most 32 characters")
    @Column(length = 32)
    private String address;

    @Size(max = 16, message = "City must be at most 16 characters")
    @Column(length = 16)
    private String city;

    // Default constructor (JPA için gerekli)
    public Customer() {
    }

    public Customer(String name, String address, String city) {
        this.name = name;
        this.address = address;
        this.city = city;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
