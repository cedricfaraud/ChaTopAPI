package com.openclassrooms.ChaTopAPI.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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
        logger.trace("Create message : " + messageDto);
        Message messageToSend = dtoToEntity(messageDto);
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
        PropertyMap<MessageDto, Message> clientPropertyMap = new PropertyMap<MessageDto, Message>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(clientPropertyMap);

        return modelMapper.map(messageDto, Message.class);

    }
}
