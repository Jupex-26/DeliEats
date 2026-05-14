package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    Page<Mensaje> findByEmisor_IdAndReceptor_IdOrEmisor_IdAndReceptor_IdOrderByFechaAsc(
            Long id1, Long id2, Long id2_rep, Long id1_rep, Pageable pageable
    );

    Page<Mensaje> findByEmisor_Id(Long emisorId, Pageable pageable);

    Page<Mensaje> findByReceptor_Id(Long receptorId, Pageable pageable);

    Long countByReceptor_IdAndLeidoFalse(Long receptorId);
}
