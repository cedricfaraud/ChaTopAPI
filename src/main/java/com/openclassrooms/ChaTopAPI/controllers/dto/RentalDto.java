package com.openclassrooms.ChaTopAPI.controllers.dto;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.ChaTopAPI.model.Rental;

import lombok.Data;

@Data
public class RentalDto {

    private String name;

    private double surface;

    private double price;

    private MultipartFile picture;

    private String description;

    public Rental rentalDtoToRental() {
        Rental rental = new Rental();
        rental.setName(getName());
        rental.setPrice(getPrice());
        rental.setSurface(getSurface());
        rental.setDescription(getDescription());
        rental.setPicture(savePictureToDisk());
        rental.setCreatedAt(Timestamp.from(Instant.now()));
        rental.setUpdatedAt(Timestamp.from(Instant.now()));
        return rental;
    }

    private String savePictureToDisk() {
        String file = "c:\\Rep";
        return file;
    }
}
