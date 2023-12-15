package com.openclassrooms.ChaTopAPI.controllers.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RentalDto {

    private String name;

    private double surface;

    private double price;

    private MultipartFile picture;

    private String description;

}
