package com.myplaylist.app.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class PageController {


    @GetMapping("/register")
    public String registerPage() {

        return "Register";
    }

    @GetMapping("/login")
    public String loginPage() {

        return "Login";
    }

    @GetMapping("/home")
    public String homePage(@RequestParam(required = false) Long id) {

        return "Home";
    }







}
