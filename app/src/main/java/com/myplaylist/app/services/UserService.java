package com.myplaylist.app.services;


import com.myplaylist.app.dtos.user.CreateUserDTO;
import com.myplaylist.app.dtos.user.LoginRequestDTO;
import com.myplaylist.app.dtos.user.UserDTO;
import com.myplaylist.app.models.User;
import com.myplaylist.app.repositories.UserRepository;
import com.myplaylist.app.repositories.VideoRepository;
import com.myplaylist.app.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final UserValidator userValidator;


    @Transactional
    public void createUser(CreateUserDTO createUserDTO) throws IllegalArgumentException {

        try {

            userValidator.validateUsernameIsUnique(createUserDTO.getUsername());

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder().
                username(createUserDTO.getUsername()).
                password(createUserDTO.getPassword()).
                build();

        userRepository.save(user);
    }

    public Long loginUser(LoginRequestDTO loginRequestDTO) throws IllegalArgumentException {

        User user = userRepository.findByUsernameAndPassword(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Error de credenciales."));

        return user.getId();
    }

    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .likedVideoIds(user.getLikedVideoIds())
                .build();
    }





}
