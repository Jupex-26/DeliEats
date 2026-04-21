package org.example.ordersservice.services;

import org.example.ordersservice.models.Repartidor;

import java.util.List;

public interface RepartidorService {

    Repartidor save(Repartidor repartidor);

    List<Repartidor> findAll();

    Repartidor findById(Long id);

    List<Repartidor> findByDisponible(boolean disponible);

    List<Repartidor> findByVehiculoTipo(String tipoVehiculo);

    Repartidor update(Long id, Repartidor repartidor);

    void deleteById(Long id);

    Repartidor updateDisponibilidad(Long id, boolean disponible);
}