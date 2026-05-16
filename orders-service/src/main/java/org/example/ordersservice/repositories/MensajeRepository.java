package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    Page<Mensaje> findByEmisor_IdAndReceptor_IdOrEmisor_IdAndReceptor_IdOrderByFechaAsc(
            Long id1, Long id2, Long id2_rep, Long id1_rep, Pageable pageable
    );

}
