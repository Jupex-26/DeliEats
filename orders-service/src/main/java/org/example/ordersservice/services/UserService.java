package org.example.ordersservice.services;

import org.example.ordersservice.models.User;

import java.util.List;

public interface UserService {

    User save(User user);

    List<User> findAll();

    User findById(Long id);

    User findByEmail(String email);

    User findByUsername(String username);

    User update(Long id, User user);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    void updatePassword(Long id, String newPassword);
}