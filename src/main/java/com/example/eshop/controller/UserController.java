package com.example.eshop.controller;

import com.example.eshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerNewUser(@RequestParam(name = "firstname") String firstName,
                                  @RequestParam(name = "lastname") String lastName,
                                  @RequestParam(name = "phonenumber") String phoneNumber,
                                  @RequestParam(name = "address") String address,
                                  @RequestParam(name = "email") String email,
                                  @RequestParam(name = "password") String password,
                                  Model model) {
        try {
            userService.registerNewUser(firstName, lastName, phoneNumber, address, email, password);
        } catch (Exception e) {
            model.addAttribute("message", "EMAIL EXISTS !");
            return "/register";
        }

        return "redirect:/login";
    }
}
