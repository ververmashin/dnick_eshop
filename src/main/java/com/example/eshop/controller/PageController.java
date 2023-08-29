package com.example.eshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping(path = {"/index","/"})
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }
}
