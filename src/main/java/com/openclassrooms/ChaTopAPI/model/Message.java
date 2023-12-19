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
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @JsonProperty(value = "rental_id")
    private Long rentalId;

    @JsonProperty(value = "user_id")
    private Long userId;

    @JsonProperty(value = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @JsonProperty(value = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
