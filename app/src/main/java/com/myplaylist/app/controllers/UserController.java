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

        try {

            userService.createUser(createUserDTO);
            return ResponseEntity.ok("User created successfully");

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(),  HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginAuth(@RequestBody LoginRequestDTO loginRequestDTO) {

        try {

            Long userId = userService.loginUser(loginRequestDTO);

            return ResponseEntity.ok(userId);

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(),  HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/me/{id}")
    public ResponseEntity<?> me(@PathVariable Long id) {

        try {

            UserDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(userDTO);

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(),  HttpStatus.NOT_FOUND);
        }
    }








}
