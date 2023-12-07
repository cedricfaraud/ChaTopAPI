package com.openclassrooms.ChaTopAPI.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.openclassrooms.ChaTopAPI.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}
