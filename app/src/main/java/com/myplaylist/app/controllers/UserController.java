package com.myplaylist.app.controllers;


import com.myplaylist.app.dtos.user.CreateUserDTO;
import com.myplaylist.app.dtos.user.LoginRequestDTO;
import com.myplaylist.app.dtos.user.UserDTO;
import com.myplaylist.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDTO createUserDTO) {



        userService.createUser(createUserDTO);
        return ResponseEntity.ok("User created successfully");


    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginAuth(@RequestBody LoginRequestDTO loginRequestDTO) {



        Long userId = userService.loginUser(loginRequestDTO);

        return ResponseEntity.ok(userId);

    }


    @GetMapping("/me/{id}")
    public ResponseEntity<?> me(@PathVariable Long id) {

        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);

    }








}
