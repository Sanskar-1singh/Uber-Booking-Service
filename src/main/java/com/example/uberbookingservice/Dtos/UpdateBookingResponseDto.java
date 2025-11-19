package com.example.uberbookingservice.Dtos;


import com.example.uberentityservices.models.BookingStatus;
import com.example.uberentityservices.models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingResponseDto {

    private Long  bookingId;
    private BookingStatus bookingStatus;
    private Optional<Driver> driver;
}
