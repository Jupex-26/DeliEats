package org.example.ordersservice.services;

import org.example.ordersservice.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User save(User user);

    Page<User> findAll(Pageable pageable);

    User findById(Long id);

    User findByEmail(String email);

    User findByUsername(String username);

    User update(Long id, User user);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    void updatePassword(Long id, String newPassword);

    User uploadFoto(Long id, MultipartFile archivo);
}