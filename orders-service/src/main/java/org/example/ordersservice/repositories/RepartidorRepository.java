package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

    Optional<Repartidor> findByEmail(String email);

    // EL MÁS IMPORTANTE: Para encontrar repartidores libres para asignarles un pedido
    List<Repartidor> findByDisponibleTrue();

}