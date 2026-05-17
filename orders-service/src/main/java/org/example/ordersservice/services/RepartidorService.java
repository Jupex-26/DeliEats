package org.example.ordersservice.services;

import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Repartidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepartidorService {

    Repartidor save(Repartidor repartidor);

    Page<Repartidor> findAll(Pageable pageable);

    Repartidor findById(Long id);
    
    Repartidor findByClienteId(Long clienteId);

    Page<Repartidor> findByDisponible(boolean disponible, Pageable pageable);
    
    Page<Repartidor> findByAprobado(boolean aprobado, Pageable pageable);

    Page<Repartidor> findByAprobado(boolean aprobado, String search, Pageable pageable);

    Repartidor update(Long id, Repartidor repartidor);

    void deleteById(Long id);

    Repartidor updateDisponibilidad(Long clienteId, boolean disponible);

    void createFromCliente(Cliente cliente);

    void aprobarRepartidor(Long id, boolean aprobado);

    boolean existsById(Long id);
    
    boolean existsByClienteId(Long clienteId);
    
    boolean isRepartidor(Long clienteId);
}
