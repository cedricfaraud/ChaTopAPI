package com.openclassrooms.ChaTopAPI.services;

import java.sql.Timestamp;
import java.time.Instant;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.ChaTopAPI.controllers.dto.MessageDto;
import com.openclassrooms.ChaTopAPI.model.Message;
import com.openclassrooms.ChaTopAPI.repository.MessageRepository;

import lombok.Data;

@Data
@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper modelMapper;

    public String saveMessage(MessageDto messageDto) {
        String message = "Message send with success";
        logger.error("Create message : " + messageDto);
        Message messageToSend = dtoToEntity(messageDto);
        messageToSend.setCreatedAt(Timestamp.from(Instant.now()));
        messageToSend.setUpdatedAt(Timestamp.from(Instant.now()));

        messageRepository.save(messageToSend);

        return message;
    }

    /**
     * Converts a Message entity to his dto.
     *
     * @param message The Message entity to be converted.
     * @return The Dto representation of the Message entity.
     */
    private Message dtoToEntity(MessageDto messageDto) {
        return modelMapper.map(messageDto, Message.class);
    }
}
