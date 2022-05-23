package com.blockwit.booking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/app/login")
    public String home() {
        return "front/login";
    }
}