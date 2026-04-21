package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.repositories.RepartidorRepository;
import org.example.ordersservice.services.RepartidorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements RepartidorService {
    private final RepartidorRepository repartidorRepository;

    @Override
    public Repartidor save(Repartidor repartidor) {
        return null;
    }

    @Override
    public List<Repartidor> findAll() {
        return List.of();
    }

    @Override
    public Repartidor findById(Long id) {
        return null;
    }

    @Override
    public List<Repartidor> findByDisponible(boolean disponible) {
        return List.of();
    }

    @Override
    public List<Repartidor> findByVehiculoTipo(String tipoVehiculo) {
        return List.of();
    }

    @Override
    public Repartidor update(Long id, Repartidor repartidor) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Repartidor updateDisponibilidad(Long id, boolean disponible) {
        return null;
    }


}