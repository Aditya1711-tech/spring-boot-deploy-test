package com.tiffin.foodDelivery.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class test {

    @GetMapping("/health-test")
    public String healthCheck(){
        return "Server is healthy";
    }
}
