package com.openclassrooms.ChaTopAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.openclassrooms.ChaTopAPI.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Query(value = "DELETE FROM messages WHERE rental_id = :rentalId", nativeQuery = true)
    void deleteMessageByRentalId(Long rentalId);
}
