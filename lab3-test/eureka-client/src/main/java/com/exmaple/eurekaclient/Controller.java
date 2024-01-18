package com.exmaple.eurekaclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Value("${eureka.instance.instance-id}")
    private String id ;

    @GetMapping("/test")
    String test() {
        return id;
    }
}
