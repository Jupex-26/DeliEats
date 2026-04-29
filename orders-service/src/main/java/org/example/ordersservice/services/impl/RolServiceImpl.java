package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.repositories.RolRepository;
import org.example.ordersservice.services.RolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Page<Rol> findAll(Pageable pageable) {
        return rolRepository.findAll(pageable);
    }

    @Override
    public Rol findById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
    }

    @Override
    public Rol findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con nombre: " + nombre));
    }

    @Override
    public Rol update(Long id, Rol rol) {
        Rol existingRol = findById(id);
        rol.setId(existingRol.getId());
        return rolRepository.save(rol);
    }

    @Override
    public void deleteById(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return rolRepository.findByNombre(nombre).isPresent();
    }
}