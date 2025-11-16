package com.example.uberbookingservice.Dtos;


import com.example.uberentityservices.models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBookingDto {
    private Long bookingId;
    private String bookingStatus;

    private Optional<Driver> driver;
}
