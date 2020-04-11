package com.example.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping("/home")
    public String home() {

        return "Home";
    }


    @GetMapping("/home2")
    public String home2() {

        return "Home2";
    }
}
