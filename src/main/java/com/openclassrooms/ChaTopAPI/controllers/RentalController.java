package com.openclassrooms.ChaTopAPI.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ChaTopAPI.controllers.dto.RentalDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.MessageResponse;
import com.openclassrooms.ChaTopAPI.model.Rental;
import com.openclassrooms.ChaTopAPI.services.RentalService;

@RestController
public class RentalController {

    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);
    private RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // @PostMapping(value = "/api/rentals", consumes = { "multipart/form-data" })
    @RequestMapping(path = "/api/rentals", method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<MessageResponse> createRental(@ModelAttribute RentalDto rentalDto) {
        logger.error("Create rental " + rentalDto);
        // logger.error("bearerToken " + bearerToken);

        Rental rentalSent = rentalService.saveRental(rentalDto);
        logger.error("rentalSent : " + rentalSent);
        MessageResponse response = new MessageResponse();
        response.setMessage("Message send with success");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
