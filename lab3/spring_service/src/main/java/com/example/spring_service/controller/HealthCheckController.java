//package com.example.spring_service.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Slf4j
//public class HealthCheckController {
//    @Value("${server.port}")
//    private String serverPort;
//
//    @RequestMapping("/health")
//    public String check() {
//        log.info(String.format("Health check received at: %s", serverPort));
//        return String.format("All good from: %s", serverPort);
//    }
//
//}
