package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.repositories.EstadoRepository;
import org.example.ordersservice.services.EstadoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoServiceImpl implements EstadoService {
    private final EstadoRepository estadoRepository;

    @Override
    public Estado save(Estado estado) {
        return null;
    }

    @Override
    public List<Estado> findAll() {
        return List.of();
    }

    @Override
    public Estado findById(Long id) {
        return null;
    }

    @Override
    public Estado findByNombre(String nombre) {
        return null;
    }

    @Override
    public Estado update(Long id, Estado estado) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public boolean existsByNombre(String nombre) {
        return false;
    }
}
