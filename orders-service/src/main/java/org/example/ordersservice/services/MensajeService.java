package org.example.ordersservice.services;

import org.example.ordersservice.models.Mensaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MensajeService {

    Mensaje save(Mensaje mensaje);

    Page<Mensaje> findAll(Pageable pageable);

    Mensaje findById(Long id);

    Page<Mensaje> findChat(Long usuario1Id, Long usuario2Id, Pageable pageable);

    void deleteById(Long id);

    void markAsRead(Long id);
    
    void validateUsersRole(Long emisorId, Long receptorId);
}
