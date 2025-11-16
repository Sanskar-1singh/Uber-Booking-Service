package com.example.uberbookingservice.Repository;

import com.example.uberentityservices.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository  extends JpaRepository<Passenger, Long> {
}
