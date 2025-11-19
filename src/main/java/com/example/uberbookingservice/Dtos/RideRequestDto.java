package com.example.uberbookingservice.Dtos;


import com.example.uberentityservices.models.ExactLocations;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDto {

   private Long passengerId;
    private ExactLocations startLocation;
    private ExactLocations endLocation;
    private List<Long> driverId;
}
