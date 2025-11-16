package com.example.uberbookingservice.Controller;


import com.example.uberbookingservice.Dtos.CreateBookingDto;
import com.example.uberbookingservice.Dtos.ResponseBookingDto;
import com.example.uberbookingservice.Service.BookingServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/v1/bookings")
public class BookingController {

    private BookingServiceImpl  bookingService;
    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;

    }

    @PostMapping
    public ResponseEntity<ResponseBookingDto> createBooking(@RequestBody CreateBookingDto createBookingDto) {
       return new ResponseEntity<>(this.bookingService.createBooking(createBookingDto), HttpStatus.CREATED);
    }
}
