package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.User;
import org.example.ordersservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("password");
    }

    @Test
    void save_Success() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void save_EmailExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        assertThrows(EmailExistsException.class, () -> userService.save(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(userRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User result = userService.findById(id);
        assertNotNull(result);
        verify(userRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void findByEmail_Found() {
        String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        User result = userService.findByEmail(email);
        assertNotNull(result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_NotFound() {
        String email = "notfound@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void update_Success() {
        Long id = 1L;
        User existingUser = new User();
        existingUser.setEmail("old@test.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.update(id, user);

        assertNotNull(result);
        assertEquals("newEncodedPassword", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_EmailExists() {
        Long id = 1L;
        User existingUser = new User();
        existingUser.setEmail("old@test.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(EmailExistsException.class, () -> userService.update(id, user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteById_Success() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(id);
        userService.deleteById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteById_NotFound() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.deleteById(id));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void updatePassword() {
        Long id = 1L;
        String newPassword = "newPassword123";
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updatePassword(id, newPassword);

        assertEquals("newEncodedPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void uploadFoto() throws IOException {
        Long id = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.uploadFoto(id, file);

        assertNotNull(result.getFoto());
        verify(userRepository).save(user);
        
        Files.deleteIfExists(Path.of("uploads", result.getFoto()));
    }
}
