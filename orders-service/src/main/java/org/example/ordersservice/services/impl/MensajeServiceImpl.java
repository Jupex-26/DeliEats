package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.repositories.MensajeRepository;
import org.example.ordersservice.services.MensajeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository mensajeRepository;

    @Override
    public Mensaje save(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    @Override
    public Page<Mensaje> findAll(Pageable pageable) {
        return mensajeRepository.findAll(pageable);
    }

    @Override
    public Mensaje findById(Long id) {
        return mensajeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mensaje no encontrado con ID: " + id));
    }

    @Override
    public Page<Mensaje> findByEmisorId(Long emisorId, Pageable pageable) {
        return mensajeRepository.findByEmisor_Id(emisorId, pageable);
    }

    @Override
    public Page<Mensaje> findByReceptorId(Long receptorId, Pageable pageable) {
        return mensajeRepository.findByReceptor_Id(receptorId, pageable);
    }


    @Override
    public void deleteById(Long id) {
        mensajeRepository.deleteById(id);
    }

    @Override
    public Long countUnreadByReceptorId(Long receptorId) {
        return mensajeRepository.countByReceptor_IdAndLeidoFalse(receptorId);
    }

    @Override
    public void markAsRead(Long id) {
        Mensaje mensaje = findById(id);
        mensaje.setLeido(true);
        mensajeRepository.save(mensaje);
    }
}
