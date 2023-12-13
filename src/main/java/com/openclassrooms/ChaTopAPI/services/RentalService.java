package com.openclassrooms.ChaTopAPI.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.ChaTopAPI.controllers.dto.RentalDto;
import com.openclassrooms.ChaTopAPI.model.Rental;
import com.openclassrooms.ChaTopAPI.model.User;
import com.openclassrooms.ChaTopAPI.repository.RentalRepository;
import com.openclassrooms.ChaTopAPI.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Optional<Rental> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public void deleteRental(final Long id) {
        rentalRepository.deleteById(id);
    }

    public Rental saveRental(RentalDto rentalDto) {
        Rental rental = dtoToEntity(rentalDto);

        rental.setOwnerId(getUserId());
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        return userId;
    }

    /**
     * Converts a Message entity to his dto.
     *
     * @param message The Message entity to be converted.
     * @return The Dto representation of the Message entity.
     */
    private Rental dtoToEntity(RentalDto rentalDto) {
        return modelMapper.map(rentalDto, Rental.class);
    }
}
