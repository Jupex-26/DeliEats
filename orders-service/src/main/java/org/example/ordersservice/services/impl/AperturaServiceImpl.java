package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.repositories.AperturaRepository;
import org.example.ordersservice.services.AperturaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AperturaServiceImpl implements AperturaService {
    private final AperturaRepository aperturaRepository;

    @Override
    public Apertura save(Apertura apertura) {
        return aperturaRepository.save(apertura);
    }

    @Override
    public Page<Apertura> findAll(Pageable pageable) {
        return aperturaRepository.findAll(pageable);
    }

    @Override
    public Apertura findById(Long id) {
        return aperturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Apertura no encontrada con ID: " + id));
    }

    @Override
    public void deleteById(Long id) {
        aperturaRepository.deleteById(id);
    }

    @Override
    public Apertura update(Long id, Apertura apertura) {
        Apertura aperturaUpdated = findById(id);

        apertura.setId(aperturaUpdated.getId());

        return aperturaRepository.save(apertura);
    }
}
