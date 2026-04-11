package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    /**
     * Busca el chat entre dos usuarios.
     * Basado en emisor_id y receptor_id de tu diagrama.
     */
    List<Mensaje> findByEmisor_IdAndReceptor_IdOrEmisor_IdAndReceptor_IdOrderByFechaAsc(
            Long id1, Long id2, Long id2_rep, Long id1_rep
    );
}
