package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.repositories.EstadoRepository;
import org.example.ordersservice.services.EstadoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;

    @Override
    public Estado save(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Override
    public Page<Estado> findAll(Pageable pageable) {
        return estadoRepository.findAll(pageable);
    }

    @Override
    public Estado findById(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estado no encontrado con ID: " + id));
    }

    @Override
    public Estado findByNombre(String nombre) {
        return estadoRepository.findByNombre(nombre)
                .orElseThrow(() -> new NotFoundException("Estado no encontrado con nombre: " + nombre));
    }

    @Override
    public Estado update(Long id, Estado estado) {
        Estado existingEstado = findById(id);
        estado.setId(existingEstado.getId());
        return estadoRepository.save(estado);
    }

    @Override
    public void deleteById(Long id) {
        estadoRepository.deleteById(id);
    }

}
