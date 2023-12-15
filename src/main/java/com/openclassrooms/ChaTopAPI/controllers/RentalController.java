package com.openclassrooms.ChaTopAPI.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ChaTopAPI.controllers.dto.RentalDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.MessageResponse;
import com.openclassrooms.ChaTopAPI.model.User;
import com.openclassrooms.ChaTopAPI.services.RentalService;
import com.openclassrooms.ChaTopAPI.services.UserService;

@RestController
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
     * @return
     */
    @PostMapping(value = "/api/rentals", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> createRental(
            @ModelAttribute RentalDto rentalDto,
            Authentication authentication) {
        // token generate with email info, getName return email info
        User user = userService.getUserByEmail(authentication.getName());

        rentalService.saveRental(rentalDto, user);
        MessageResponse response = new MessageResponse();
        response.setMessage("Rental created !");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
