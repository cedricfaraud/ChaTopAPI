package com.openclassrooms.ChaTopAPI.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ChaTopAPI.controllers.dto.RentalDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.MessageResponse;
import com.openclassrooms.ChaTopAPI.controllers.responses.RentalsResponse;
import com.openclassrooms.ChaTopAPI.model.Rental;
import com.openclassrooms.ChaTopAPI.model.User;
import com.openclassrooms.ChaTopAPI.services.RentalService;
import com.openclassrooms.ChaTopAPI.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Rental")
public class RentalController {

    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);

    @Autowired
    UserService userService;

    @Autowired
    RentalService rentalService;

    /**
     * Create rental
     * 
     * @param rentalDto
     * @param authentication
     * @return ack create message
     */
    @PostMapping(value = "/api/rentals", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new rental", description = "Create a new rental with the specified informations")
    public ResponseEntity<MessageResponse> createRental(
            @ModelAttribute RentalDto rentalDto,
            Authentication authentication) {
        MessageResponse response = new MessageResponse();
        logger.trace("Rental to create : " + rentalDto);
        logger.trace("Rental to create (authentication) : " + authentication);
        try {
            // token generate with email info, getName return email info
            User user = userService.getUserByEmail(authentication.getName());
            rentalService.saveRental(rentalDto, user);
            response.setMessage("Rental created !");
            logger.info("Rental's user : " + authentication.getName());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            logger.info("Error rental's user not found !");
            response.setMessage("Error rental's user not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Create rental
     * 
     * @param rentalDto
     * @param authentication
     * @return ack update message
     */
    @PutMapping(value = "/api/rentals/{id}", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update rental by id", description = "Update a rental with the rental id and specified informations")
    public ResponseEntity<MessageResponse> updateRental(
            @ModelAttribute RentalDto rentalDto) {
        logger.trace("Rental to update : " + rentalDto);
        MessageResponse response = new MessageResponse();

        rentalService.updateRental(rentalDto);
        response.setMessage("Rental updated !");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Delete rental by id (for future use)
     * 
     * @param id
     * @param authentication
     * @return ack delete message
     */
    @DeleteMapping(value = "/api/rentals/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete rental by id", description = "Delete a rental with the rental id")
    public ResponseEntity<MessageResponse> deleteRental(@PathVariable Long id, Authentication authentication) {

        // future upgrade to delete rental
        MessageResponse response = new MessageResponse();
        logger.trace("Rental to delete : " + id);
        try {
            // token generate with email info, getName return email info
            User user = userService.getUserByEmail(authentication.getName());
            logger.trace("User's rental to delete : " + user);
            String result = rentalService.deleteRental(id, user);
            response.setMessage(result);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (NoSuchElementException e) {
            logger.error("Error rental's user not found !");
            response.setMessage("Error rental's user not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Get all rentals
     * 
     * @return all rentals
     */
    @GetMapping(value = "/api/rentals", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get list rentals", description = "Retrieve information of all rentals")
    public RentalsResponse getAllRentals() {

        RentalsResponse rentals = rentalService.getRentals();

        logger.trace("All rentals : " + rentals);
        return rentals;
    }

    /**
     * Get rental by id
     * 
     * @param id
     * @return rental
     */
    @GetMapping(value = "/api/rentals/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get rental by id", description = "Retrieve rental information specified by his id")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        try {
            logger.trace("Get rental id : " + id);
            Rental rental = rentalService.getRental(id);
            logger.trace("Get rental by id result : " + rental);
            return new ResponseEntity<Rental>(rental, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Rental>(HttpStatus.NOT_FOUND);
        }
    }
}
