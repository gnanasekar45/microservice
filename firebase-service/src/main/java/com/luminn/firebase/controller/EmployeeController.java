package com.luminn.firebase.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @GetMapping("/")
    public String hello() {
        return "Version 5 service is up and running 1";
    }


}
