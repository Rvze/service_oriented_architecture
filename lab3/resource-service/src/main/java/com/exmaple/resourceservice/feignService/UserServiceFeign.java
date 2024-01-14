package com.exmaple.resourceservice.feignService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "booking-service")
public interface UserServiceFeign {
    @GetMapping("/test")
    String test();
}
