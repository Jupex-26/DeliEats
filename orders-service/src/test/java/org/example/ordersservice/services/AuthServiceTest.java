package org.example.ordersservice.services;

import org.example.ordersservice.dtos.auth.LoginRequestDto;
import org.example.ordersservice.dtos.auth.LoginResponseDto;
import org.example.ordersservice.dtos.user.UserOutputDto;
import org.example.ordersservice.exception.custom.UnauthorizedException;
import org.example.ordersservice.mappers.UserMapper;
import org.example.ordersservice.models.User;
import org.example.ordersservice.security.JwtService;
import org.example.ordersservice.services.impl.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_Success() {
        LoginRequestDto request = new LoginRequestDto("test@test.com", "password");
        User user = new User();
        UserOutputDto userOutputDto = new UserOutputDto();
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(customUserDetailsService.loadUserByUsername(request.getEmail())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("test-token");
        when(userMapper.toDto(user)).thenReturn(userOutputDto);

        LoginResponseDto response = authService.login(request);

        assertNotNull(response);
        assertEquals("test-token", response.getToken());
        assertEquals(userOutputDto, response.getUserOutputDto());
    }

    @Test
    void login_Failure_BadCredentials() {
        LoginRequestDto request = new LoginRequestDto("test@test.com", "wrong-password");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }
}
