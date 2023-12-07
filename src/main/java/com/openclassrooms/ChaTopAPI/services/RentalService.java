package com.openclassrooms.ChaTopAPI.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.ChaTopAPI.model.Rental;
import com.openclassrooms.ChaTopAPI.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public Optional<Rental> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public void deleteRental(final Long id) {
        rentalRepository.deleteById(id);
    }

    public Rental saveRental(Rental rental) {
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }

}
