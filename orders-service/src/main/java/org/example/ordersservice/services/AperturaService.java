package org.example.ordersservice.services;

import org.example.ordersservice.models.Apertura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AperturaService {

    Apertura save(Apertura apertura);

    Page<Apertura> findAll(Pageable pageable);

    Apertura findById(Long id);

    void deleteById(Long id);

    Apertura update(Long id, Apertura apertura);
}
