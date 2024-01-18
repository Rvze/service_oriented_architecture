package com.exmaple.bookingserviceonspingcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BookingServiceOnSpingCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceOnSpingCloudApplication.class, args);
    }

}
