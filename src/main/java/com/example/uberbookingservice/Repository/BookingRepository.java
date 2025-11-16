package com.example.uberbookingservice.Repository;


import com.example.uberentityservices.models.BookingStatus;
import com.example.uberentityservices.models.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Bookings,Long> {


}
