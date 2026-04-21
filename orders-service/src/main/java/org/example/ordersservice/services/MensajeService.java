package org.example.ordersservice.services;

import org.example.ordersservice.models.Mensaje;

import java.util.List;

public interface MensajeService {

    Mensaje save(Mensaje mensaje);

    List<Mensaje> findAll();

    Mensaje findById(Long id);

    List<Mensaje> findByEmisorId(Long emisorId);

    List<Mensaje> findByReceptorId(Long receptorId);

    List<Mensaje> findByConversacionId(Long conversacionId);

    void deleteById(Long id);

    Long countUnreadByReceptorId(Long receptorId);

    void markAsRead(Long id);
}