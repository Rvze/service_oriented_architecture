package com.exmaple.resourceservice.controllers;

import com.exmaple.resourceservice.feignService.UserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResourceController {
    @Autowired
    private UserServiceFeign userServiceFeign;
    @GetMapping("/test")
    public String test() {
        return userServiceFeign.test();
    }
}
