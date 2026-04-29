package org.example.ordersservice.repositories;


import org.example.ordersservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Vital para el proceso de Login (Spring Security)
    Optional<User> findByEmail(String email);

    // Para verificar si un email ya está registrado antes de crear un nuevo usuario
    boolean existsByEmail(String email);

    // Si quisieras buscar usuarios por su rol (ej: todos los ADMIN)
    List<User> findByRolNombre(String nombreRol);
}