package com.myplaylist.app;

import com.myplaylist.app.dtos.user.CreateUserDTO;
import com.myplaylist.app.dtos.user.LoginRequestDTO;
import com.myplaylist.app.dtos.user.UserDTO;
import com.myplaylist.app.models.User;
import com.myplaylist.app.repositories.UserRepository;
import com.myplaylist.app.services.UserService;
import com.myplaylist.app.validators.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_Success() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .username("testuser")
                .password("password123")
                .build();

        doNothing().when(userValidator).validateUsernameIsUnique(dto.getUsername());
        when(userRepository.save(any(User.class))).thenReturn(new User());

        assertDoesNotThrow(() -> userService.createUser(dto));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_UsernameExists_ThrowsException() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .username("existinguser")
                .password("password123")
                .build();

        doThrow(new IllegalArgumentException("Username already exists"))
                .when(userValidator).validateUsernameIsUnique(dto.getUsername());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .username("testuser")
                .password("password123")
                .build();

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .build();

        when(userRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword()))
                .thenReturn(Optional.of(user));

        Long userId = userService.loginUser(dto);

        assertEquals(1L, userId);
    }

    @Test
    void loginUser_InvalidCredentials_ThrowsException() {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

        when(userRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.loginUser(dto));
    }

    @Test
    void getUserById_Success() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .likedVideoIds(new ArrayList<>())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));
    }
}