package org.example.ordersservice.services;

import org.example.ordersservice.models.Estado;

import java.util.List;

public interface EstadoService {

    Estado save(Estado estado);

    List<Estado> findAll();

    Estado findById(Long id);

    Estado findByNombre(String nombre);

    Estado update(Long id, Estado estado);

    void deleteById(Long id);

    boolean existsByNombre(String nombre);
}