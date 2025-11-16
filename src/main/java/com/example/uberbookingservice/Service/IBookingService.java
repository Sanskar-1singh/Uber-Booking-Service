package com.example.uberbookingservice.Service;

import com.example.uberbookingservice.Dtos.CreateBookingDto;
import com.example.uberbookingservice.Dtos.ResponseBookingDto;
import com.example.uberentityservices.models.Bookings;

public interface IBookingService  {

    public ResponseBookingDto createBooking(CreateBookingDto bookings);

}
