package com.example.uberbookingservice.Service;


import com.example.uberbookingservice.Dtos.CreateBookingDto;
import com.example.uberbookingservice.Dtos.DriverLocationDto;
import com.example.uberbookingservice.Dtos.NearByDriverRequestDto;
import com.example.uberbookingservice.Dtos.ResponseBookingDto;
import com.example.uberbookingservice.Repository.BookingRepository;
import com.example.uberbookingservice.Repository.PassengerRepository;
import com.example.uberentityservices.models.BookingStatus;
import com.example.uberentityservices.models.Bookings;
import com.example.uberentityservices.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements IBookingService{
    private final BookingRepository bookingRepository;
    private PassengerRepository passengerRepository;

    private RestTemplate restTemplate=new RestTemplate();

    private final static String LOCATION_SERVICE="http://localhost:7777";


    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
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

        ResponseEntity<DriverLocationDto> driver=restTemplate.postForEntity(
                LOCATION_SERVICE+"/api/location/nearby/drivers"
                ,nearByDriverRequestDto, DriverLocationDto.class);


        List<DriverLocationDto> driverLocationDto= Arrays.asList(driver.getBody());

         if (driver.getStatusCode().is2xxSuccessful() && driver.getBody()!=null) {
             driverLocationDto.forEach(location -> {
                 System.out.println(location.getDriverId());
             });
         }

        return ResponseBookingDto.builder()
                .bookingId(result.getId())
                .bookingStatus(result.getBookingStatus().toString())
                .build();

    }

}
