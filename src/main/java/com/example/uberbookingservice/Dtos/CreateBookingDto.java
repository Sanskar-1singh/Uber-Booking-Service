package com.example.uberbookingservice.Dtos;


import com.example.uberentityservices.models.ExactLocations;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingDto {

    private Long passengerId;
    private ExactLocations  startLocations;
    private ExactLocations endLocations;


}
