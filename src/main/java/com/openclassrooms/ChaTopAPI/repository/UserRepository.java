package com.openclassrooms.ChaTopAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.ChaTopAPI.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    // User findByUsername(String name);

    User findByName(String name);
}
