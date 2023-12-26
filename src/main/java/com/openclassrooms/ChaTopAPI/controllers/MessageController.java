package com.openclassrooms.ChaTopAPI.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ChaTopAPI.controllers.dto.MessageDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.MessageResponse;
import com.openclassrooms.ChaTopAPI.services.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Message")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    /**
     * Create message
     * 
     * @param messageDto
     * @return ack message
     */
    @PostMapping(value = "/api/messages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new message", description = "Create a new message with the specified informations")
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageDto messageDto) {

        logger.trace("Create message : " + messageDto);
        String messageSent = messageService.saveMessage(messageDto);
        logger.trace("End Create message : " + messageSent);
        MessageResponse response = new MessageResponse();
        response.setMessage("Message send with success");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
