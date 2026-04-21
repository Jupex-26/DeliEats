package org.example.ordersservice.services;

import org.example.ordersservice.models.Rol;

import java.util.List;

public interface RolService {

    Rol save(Rol rol);

    List<Rol> findAll();

    Rol findById(Long id);

    Rol findByNombre(String nombre);

    Rol update(Long id, Rol rol);

    void deleteById(Long id);

    boolean existsByNombre(String nombre);
}