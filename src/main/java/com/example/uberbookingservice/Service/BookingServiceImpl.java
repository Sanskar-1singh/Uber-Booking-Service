package com.example.uberbookingservice.Service;


import com.example.uberbookingservice.Dtos.*;
import com.example.uberbookingservice.Repository.BookingRepository;
import com.example.uberbookingservice.Repository.DriverRepository;
import com.example.uberbookingservice.Repository.PassengerRepository;
import com.example.uberbookingservice.apis.LocationServiceApis;
import com.example.uberbookingservice.apis.UberSocketApi;
import com.example.uberentityservices.models.BookingStatus;
import com.example.uberentityservices.models.Bookings;
import com.example.uberentityservices.models.Driver;
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
    private final DriverRepository driverRepository;
    private PassengerRepository passengerRepository;

    private RestTemplate restTemplate=new RestTemplate();

//    private final static String LOCATION_SERVICE="http://localhost:7777";

    private LocationServiceApis  locationServiceApis;
    private UberSocketApi uberSocketApi;


    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository
                              , LocationServiceApis locationServiceApis, DriverRepository driverRepository,UberSocketApi uberSocketApi) {
        this.passengerRepository = passengerRepository;
        this.uberSocketApi=uberSocketApi;
        this.bookingRepository = bookingRepository;
        this.locationServiceApis = locationServiceApis;
        this.driverRepository = driverRepository;
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

              processNearBydriverAsync(nearByDriverRequestDto,bookingDetail.getPassengerId());

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

    @Override
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingrequestDto, Long bookingId) {

        Optional<Driver> driver=driverRepository.findById(bookingrequestDto.getDriverId().get());

            bookingRepository.updateBookingStatusAndDriverById(bookingId,bookingrequestDto.getBookingStatus(),driver.get());
            Optional<Bookings> booking=bookingRepository.findById(bookingId);
            return UpdateBookingResponseDto.builder()
                    .bookingId(bookingId)
                    .bookingStatus(booking.get().getBookingStatus())
                    .driver(Optional.of(booking.get().getDriver()))
                    .build();
    }

    private void processNearBydriverAsync(NearByDriverRequestDto nearByDriverRequestDto,Long passengerId) {

        System.out.println("From service");
        Call<DriverLocationDto[]>  call= locationServiceApis.getNearByDrivers(nearByDriverRequestDto);

        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                System.out.println("From service");
                if(response.isSuccessful() && response.body()!=null){
                     List<DriverLocationDto> driverLocationDtoList=Arrays.asList(response.body());
                     System.out.println(driverLocationDtoList);
                     driverLocationDtoList.forEach(driverLocationDto -> {
                         System.out.println(driverLocationDto.getDriverId().toString());
                     });
                     raiseRideRequestAsync(RideRequestDto.builder()
                             .passengerId(passengerId).build());
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

    private void raiseRideRequestAsync(RideRequestDto rideRequestDto){
        Call<Boolean> call= uberSocketApi.getNearByDriver(rideRequestDto);
        System.out.println("bye bye");

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                 if(response.isSuccessful() && response.body()){
                     Boolean result=response.body();
                     System.out.println("Driver response is"+result.toString());
                 }
                 else{
                     System.out.println(response.message());
                 }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                 throwable.printStackTrace();
            }
        });
    }





}

