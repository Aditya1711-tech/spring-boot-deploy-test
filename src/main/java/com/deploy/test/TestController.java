package com.deploy.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    public String helloTest(){
        return "Welcome back!!";
    }
}
