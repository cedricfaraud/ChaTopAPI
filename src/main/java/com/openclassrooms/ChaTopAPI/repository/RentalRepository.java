package com.openclassrooms.ChaTopAPI.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.openclassrooms.ChaTopAPI.model.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {
}
