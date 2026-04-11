package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    /**
     * Busca un rol por su nombre (ej: "CLIENTE").
     * Vital para asignar el rol correcto en el proceso de registro.
     */
    Optional<Rol> findByNombre(String nombre);
}