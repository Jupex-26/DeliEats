package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.repositories.RolRepository;
import org.example.ordersservice.services.RolService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;

    @Override
    public Rol save(Rol rol) {
        return null;
    }

    @Override
    public List<Rol> findAll() {
        return List.of();
    }

    @Override
    public Rol findById(Long id) {
        return null;
    }

    @Override
    public Rol findByNombre(String nombre) {
        return null;
    }

    @Override
    public Rol update(Long id, Rol rol) {
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
