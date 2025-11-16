package com.example.uberbookingservice.apis;

import com.example.uberbookingservice.Dtos.DriverLocationDto;
import com.example.uberbookingservice.Dtos.NearByDriverRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApis {

    @POST("/api/location/nearby/drivers")
    Call<DriverLocationDto[]> getNearByDrivers(@Body NearByDriverRequestDto nearByDriverRequestDto);


}
