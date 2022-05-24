package com.blockwit.booking.controllers;

import com.blockwit.booking.model.NewAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/app")
public class SecurityController {

    @GetMapping("/login")
    public String home() {
        return "front/login";
    }

    @GetMapping("/account/new")
    public ModelAndView createAccountGet() {
        return new ModelAndView("front/account-new", Map.of("newAccount", new NewAccount()));
    }

}