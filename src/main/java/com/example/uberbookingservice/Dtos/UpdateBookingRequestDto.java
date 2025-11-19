package com.example.uberbookingservice.Dtos;


import com.example.uberentityservices.models.BookingStatus;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequestDto {

    private String bookingStatus;
    private Optional<Long> driverId;

}
