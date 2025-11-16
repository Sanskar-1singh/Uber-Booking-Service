package com.example.uberbookingservice.Dtos;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NearByDriverRequestDto {

    Double latitude;
    Double longitude;

}
