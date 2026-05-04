package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.repositories.RepartidorRepository;
import org.example.ordersservice.services.RepartidorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repartidorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Repartidor save(Repartidor repartidor) {
        if (Objects.nonNull(repartidor.getPassword())) {
            repartidor.setPassword(passwordEncoder.encode(repartidor.getPassword()));
        }
        return repartidorRepository.save(repartidor);
    }

    @Override
    public Page<Repartidor> findAll(Pageable pageable) {
        return repartidorRepository.findAll(pageable);
    }

    @Override
    public Repartidor findById(Long id) {
        return repartidorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Repartidor no encontrado con ID: " + id));
    }

    @Override
    public Page<Repartidor> findByDisponible(boolean disponible, Pageable pageable) {
        return repartidorRepository.findByDisponible(disponible, pageable);
    }

    @Override
    public Repartidor update(Long id, Repartidor repartidor) {
        Repartidor existingRepartidor = findById(id);
        repartidor.setId(existingRepartidor.getId());
        
        if (Objects.nonNull(repartidor.getPassword()) && !repartidor.getPassword().isEmpty()) {
            repartidor.setPassword(passwordEncoder.encode(repartidor.getPassword()));
        } else {
            repartidor.setPassword(existingRepartidor.getPassword());
        }

        return repartidorRepository.save(repartidor);
    }

    @Override
    public void deleteById(Long id) {
        repartidorRepository.deleteById(id);
    }

    @Override
    public Repartidor updateDisponibilidad(Long id, boolean disponible) {
        Repartidor repartidor = findById(id);
        repartidor.setDisponible(disponible);
        return repartidorRepository.save(repartidor);
    }
}
