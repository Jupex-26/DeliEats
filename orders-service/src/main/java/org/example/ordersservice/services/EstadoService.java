package org.example.ordersservice.services;

import org.example.ordersservice.models.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstadoService {

    Estado save(Estado estado);

    Page<Estado> findAll(Pageable pageable);

    Estado findById(Long id);

    Estado findByNombre(String nombre);

    Estado update(Long id, Estado estado);

    void deleteById(Long id);

    boolean existsByNombre(String nombre);
}