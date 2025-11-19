package com.example.uberbookingservice.Repository;


import com.example.uberentityservices.models.BookingStatus;
import com.example.uberentityservices.models.Bookings;
import com.example.uberentityservices.models.Driver;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Bookings,Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Bookings b SET b.bookingStatus=:status,b.driver=:driver WHERE b.id=:id")
    void updateBookingStatusAndDriverById(@Param("id") Long id,@Param("status") String status,@Param("driver") Driver driver);
}
