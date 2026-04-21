package org.example.ordersservice.services;

import org.example.ordersservice.models.Apertura;

import java.util.List;

public interface AperturaService {

    Apertura save(Apertura apertura);

    List<Apertura> findAll();

    Apertura findById(Long id);

    void deleteById(Long id);

    Apertura update(Long id, Apertura apertura);
}
