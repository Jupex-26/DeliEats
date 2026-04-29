package org.example.ordersservice.services;

import org.example.ordersservice.models.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RolService {

    Rol save(Rol rol);

    Page<Rol> findAll(Pageable pageable);

    Rol findById(Long id);

    Rol findByNombre(String nombre);

    Rol update(Long id, Rol rol);

    void deleteById(Long id);

    boolean existsByNombre(String nombre);
}