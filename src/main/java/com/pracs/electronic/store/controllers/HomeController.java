package com.pracs.electronic.store.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class HomeController {

    @GetMapping
    public String testString(){
        return "Welcome to Electronic store";
    }
}
