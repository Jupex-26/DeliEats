package org.example.ordersservice.services.impl;


import org.example.ordersservice.models.User;
import org.example.ordersservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String email) {
        System.out.println("🔍 Buscando email en BD: '" + email + "'");
        System.out.println("🔍 Longitud: " + email.length());

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            System.out.println("❌ Email NO encontrado en BD");
            // Lista todos los emails para debug
            userRepository.findAll().forEach(u ->
                    System.out.println("   BD tiene: '" + u.getEmail() + "'"));
        } else {
            System.out.println("✅ Email encontrado");
        }

        return user.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
