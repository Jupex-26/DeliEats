package org.example.ordersservice.services;

import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Repartidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepartidorService {

    Repartidor save(Repartidor repartidor);

    Page<Repartidor> findAll(Pageable pageable);

    Repartidor findById(Long id);

    Page<Repartidor> findByDisponible(boolean disponible, Pageable pageable);
    
    Page<Repartidor> findByAprobado(boolean aprobado, Pageable pageable);

    Repartidor update(Long id, Repartidor repartidor);

    void deleteById(Long id);

    Repartidor updateDisponibilidad(Long id, boolean disponible);

    void createFromCliente(Cliente cliente);

    void aprobarRepartidor(Long id, boolean aprobado);

}
