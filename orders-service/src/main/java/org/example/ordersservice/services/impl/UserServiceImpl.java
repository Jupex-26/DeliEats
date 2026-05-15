package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.ConflictException;
import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.User;
import org.example.ordersservice.repositories.UserRepository;
import org.example.ordersservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String UPLOAD_DIR = "uploads";

    @Override
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistsException("El email " + user.getEmail() + " ya está registrado.");
        }
        if (Objects.nonNull(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        
        if (Objects.nonNull(user.getEmail()) && !user.getEmail().equalsIgnoreCase(existingUser.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistsException("El email " + user.getEmail() + " ya está en uso por otro usuario.");
        }
        
        user.setId(existingUser.getId());
        
        if (Objects.nonNull(user.getPassword()) && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
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
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // El método uploadFoto ya es correcto en cuanto a lógica
    public User uploadFoto(Long id, MultipartFile archivo) {
        User user = findById(id);

        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("No se ha seleccionado ningún archivo");
        }

        try {
            // 1. Crear el directorio si no existe
            Path rootPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Path filePath = rootPath.resolve(nombreUnico);

            Files.copy(archivo.getInputStream(), filePath);

            if (user.getFoto() != null) {
                Path fotoAnterior = rootPath.resolve(user.getFoto());
                Files.deleteIfExists(fotoAnterior);
            }

            user.setFoto(nombreUnico);
            return userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Error crítico al guardar la foto: " + e.getMessage());
        }
    }
}
