package org.example.ordersservice.services;

import org.example.ordersservice.models.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MensajeService {

    Mensaje save(Mensaje mensaje);

    Page<Mensaje> findAll(Pageable pageable);

    Mensaje findById(Long id);

    Page<Mensaje> findByEmisorId(Long emisorId, Pageable pageable);

    Page<Mensaje> findByReceptorId(Long receptorId, Pageable pageable);

    void deleteById(Long id);

    Long countUnreadByReceptorId(Long receptorId);

    void markAsRead(Long id);
}
