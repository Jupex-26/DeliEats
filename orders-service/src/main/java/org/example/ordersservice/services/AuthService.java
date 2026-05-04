package org.example.ordersservice.services;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.auth.LoginRequestDto;
import org.example.ordersservice.dtos.auth.LoginResponseDto;
import org.example.ordersservice.dtos.user.UserOutputDto;
import org.example.ordersservice.exception.custom.UnauthorizedException;
import org.example.ordersservice.mappers.UserMapper;
import org.example.ordersservice.models.User;
import org.example.ordersservice.security.JwtService;
import org.example.ordersservice.services.impl.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public LoginResponseDto login(LoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) customUserDetailsService.loadUserByUsername(request.getEmail());
            String jwtToken = jwtService.generateToken(user);

            return LoginResponseDto.builder()
                    .token(jwtToken)
                    .userOutputDto(userMapper.toDto(user))
                    .build();
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
    }
}
