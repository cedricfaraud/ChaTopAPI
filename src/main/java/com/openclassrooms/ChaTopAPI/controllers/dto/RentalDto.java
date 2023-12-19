package com.openclassrooms.ChaTopAPI.controllers.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class RentalDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String name;

    private double surface;

    private double price;

    private MultipartFile picture;

    private String description;

}
