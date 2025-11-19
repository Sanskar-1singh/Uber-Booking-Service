package com.example.uberbookingservice.Controller;


import com.example.uberbookingservice.Dtos.CreateBookingDto;
import com.example.uberbookingservice.Dtos.ResponseBookingDto;
import com.example.uberbookingservice.Dtos.UpdateBookingRequestDto;
import com.example.uberbookingservice.Dtos.UpdateBookingResponseDto;
import com.example.uberbookingservice.Service.BookingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/bookings")
public class BookingController {

    //to logging purpose>>>>
    private static final Logger logger= LoggerFactory.getLogger(BookingController.class);


    private BookingServiceImpl  bookingService;
    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
        logger.info("hello  error occured again");
    }

    @PostMapping
    public ResponseEntity<ResponseBookingDto> createBooking(@RequestBody CreateBookingDto createBookingDto) {
       return new ResponseEntity<>(this.bookingService.createBooking(createBookingDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<UpdateBookingResponseDto> updateBooking(@RequestBody UpdateBookingRequestDto updateBookingRequestDto, @PathVariable("bookingId") Long bookingId) {
         return new ResponseEntity<>(bookingService.updateBooking(updateBookingRequestDto, bookingId), HttpStatus.OK);
    }
}
