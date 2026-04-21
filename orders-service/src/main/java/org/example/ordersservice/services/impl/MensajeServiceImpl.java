package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.repositories.MensajeRepository;
import org.example.ordersservice.services.MensajeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {
    private final MensajeRepository mensajeRepository;

    @Override
    public Mensaje save(Mensaje mensaje) {
        return null;
    }

    @Override
    public List<Mensaje> findAll() {
        return List.of();
    }

    @Override
    public Mensaje findById(Long id) {
        return null;
    }

    @Override
    public List<Mensaje> findByEmisorId(Long emisorId) {
        return List.of();
    }

    @Override
    public List<Mensaje> findByReceptorId(Long receptorId) {
        return List.of();
    }

    @Override
    public List<Mensaje> findByConversacionId(Long conversacionId) {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Long countUnreadByReceptorId(Long receptorId) {
        return 0L;
    }

    @Override
    public void markAsRead(Long id) {

    }
}
