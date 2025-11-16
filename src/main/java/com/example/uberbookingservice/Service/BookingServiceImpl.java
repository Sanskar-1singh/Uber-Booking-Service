package com.example.uberbookingservice.Service;


import com.example.uberbookingservice.Dtos.CreateBookingDto;
import com.example.uberbookingservice.Dtos.DriverLocationDto;
import com.example.uberbookingservice.Dtos.NearByDriverRequestDto;
import com.example.uberbookingservice.Dtos.ResponseBookingDto;
import com.example.uberbookingservice.Repository.BookingRepository;
import com.example.uberbookingservice.Repository.PassengerRepository;
import com.example.uberbookingservice.apis.LocationServiceApis;
import com.example.uberentityservices.models.BookingStatus;
import com.example.uberentityservices.models.Bookings;
import com.example.uberentityservices.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements IBookingService{
    private final BookingRepository bookingRepository;
    private PassengerRepository passengerRepository;

    private RestTemplate restTemplate=new RestTemplate();

//    private final static String LOCATION_SERVICE="http://localhost:7777";

    private LocationServiceApis  locationServiceApis;


    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository
                              , LocationServiceApis locationServiceApis) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.locationServiceApis = locationServiceApis;
    }
     @Override
    public ResponseBookingDto createBooking(CreateBookingDto bookingDetail) {

         Optional<Passenger> passenger=passengerRepository.findById(bookingDetail.getPassengerId());

         Bookings booking=Bookings.builder()
                 .bookingStatus(BookingStatus.SCHEDULE)
                 .startLocations(bookingDetail.getStartLocations())
                 .endLocations(bookingDetail.getEndLocations())
                 .passenger(passenger.get())
                 .build();

        Bookings result=bookingRepository.save(booking);

        //make api call to location service to fetch nearby driver
         NearByDriverRequestDto nearByDriverRequestDto= NearByDriverRequestDto.builder()
                 .latitude(bookingDetail.getStartLocations().getLatitude())
                 .longitude(bookingDetail.getStartLocations().getLongitude())
                 .build();

              processNearBydriverAsync(nearByDriverRequestDto);

//        ResponseEntity<DriverLocationDto> driver=restTemplate.postForEntity(
//                LOCATION_SERVICE+"/api/location/nearby/drivers"
//                ,nearByDriverRequestDto, DriverLocationDto.class);
//
//
//        List<DriverLocationDto> driverLocationDto= Arrays.asList(driver.getBody());
//
//         if (driver.getStatusCode().is2xxSuccessful() && driver.getBody()!=null) {
//             driverLocationDto.forEach(location -> {
//                 System.out.println(location.getDriverId());
//             });
//         }



        return ResponseBookingDto.builder()
                .bookingId(result.getId())
                .bookingStatus(result.getBookingStatus().toString())
                .build();

    }

    private void processNearBydriverAsync(NearByDriverRequestDto nearByDriverRequestDto) {
        Call<DriverLocationDto[]>  call= locationServiceApis.getNearByDrivers(nearByDriverRequestDto);

        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                 if(response.isSuccessful() && response.body()!=null){
                     List<DriverLocationDto> driverLocationDtoList=Arrays.asList(response.body());
                     driverLocationDtoList.forEach(driverLocationDto -> {
                         System.out.println(driverLocationDto.getDriverId().toString());
                     });
                 }
                 else{
                     System.out.println(response.message());
                 }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}

