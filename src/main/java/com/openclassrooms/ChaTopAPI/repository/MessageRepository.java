package com.openclassrooms.ChaTopAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.ChaTopAPI.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
