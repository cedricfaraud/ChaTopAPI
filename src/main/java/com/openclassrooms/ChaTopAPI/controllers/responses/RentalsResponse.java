package com.openclassrooms.ChaTopAPI.controllers.responses;

import java.util.List;

import com.openclassrooms.ChaTopAPI.model.Rental;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalsResponse {
    private List<Rental> rentals;
}
