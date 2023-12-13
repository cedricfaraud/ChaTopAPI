package com.openclassrooms.ChaTopAPI.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ChaTopAPI.controllers.dto.MessageDto;
import com.openclassrooms.ChaTopAPI.controllers.responses.MessageResponse;
import com.openclassrooms.ChaTopAPI.services.MessageService;

@RestController
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/api/messages/")
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageDto messageDto) {

        logger.error("Create message : " + messageDto);
        String messageSent = messageService.saveMessage(messageDto);
        logger.error("End Create message : " + messageSent);
        MessageResponse response = new MessageResponse();
        response.setMessage("Message send with success");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
