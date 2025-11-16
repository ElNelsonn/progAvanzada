package com.myplaylist.app.validators;


import com.myplaylist.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;


    public void validateUsernameIsUnique(String username) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username en uso.");
        }

    }






}
