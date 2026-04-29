package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.ConflictException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.User;
import org.example.ordersservice.repositories.UserRepository;
import org.example.ordersservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("El email ya está en uso: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + email));
    }

    @Override
    public User findByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con: " + email));
    }

    @Override
    public User update(Long id, User user) {
        User existingUser = findById(id);
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("El email ya está en uso: " + user.getEmail());
        }
        user.setId(existingUser.getId());
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        User user = findById(id);
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
