package com.example.uberbookingservice.Controller;


import com.example.uberbookingservice.apis.LocationServiceApis;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Autowired
    private EurekaClient eurekaClient;

     private String getServerName(String serviceName){

         return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
     }
     
     @Bean
    public LocationServiceApis locationServiceApis(){
         System.out.println("Inside locationServiceApis"+getServerName("UBER-LOCATION-SERVICES"));
         return new Retrofit.Builder()
                 .baseUrl(getServerName("UBER-LOCATION-SERVICES"))
                 .addConverterFactory(GsonConverterFactory.create())
                 .client(new OkHttpClient.Builder().build())
                 .build()
                 .create(LocationServiceApis.class);


     }


}
