package com.openclassrooms.ChaTopAPI.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.ChaTopAPI.model.Message;
import com.openclassrooms.ChaTopAPI.repository.MessageRepository;

import lombok.Data;

@Data
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }
}
