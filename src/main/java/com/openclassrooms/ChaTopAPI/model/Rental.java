package com.openclassrooms.ChaTopAPI.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double surface;

    private double price;

    private String picture;

    private String description;

    @JsonProperty("owner_id")
    private Long ownerId;

    @JsonProperty(value = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @JsonProperty(value = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
