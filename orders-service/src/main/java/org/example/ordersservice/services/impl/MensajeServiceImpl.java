package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.ConflictException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.models.User;
import org.example.ordersservice.repositories.MensajeRepository;
import org.example.ordersservice.services.MensajeService;
import org.example.ordersservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository mensajeRepository;
    private final UserService userService;

    @Override
    public Mensaje save(Mensaje mensaje) {
        validateUsersRole(mensaje.getEmisor().getId(), mensaje.getReceptor().getId());
        
        mensaje.setEmisor(userService.findById(mensaje.getEmisor().getId()));
        mensaje.setReceptor(userService.findById(mensaje.getReceptor().getId()));

        if (mensaje.getFecha() == null) {
            mensaje.setFecha(LocalDateTime.now());
        }

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
    public Page<Mensaje> findChat(Long usuario1Id, Long usuario2Id, Pageable pageable) {
        validateUsersRole(usuario1Id, usuario2Id);
        return mensajeRepository.findByEmisor_IdAndReceptor_IdOrEmisor_IdAndReceptor_IdOrderByFechaAsc(
                usuario1Id, usuario2Id, usuario2Id, usuario1Id, pageable
        );
    }


    @Override
    public void deleteById(Long id) {
        mensajeRepository.deleteById(id);
    }


    @Override
    public void markAsRead(Long id) {
        Mensaje mensaje = findById(id);
        mensaje.setLeido(true);
        mensajeRepository.save(mensaje);
    }
    
    @Override
    public void validateUsersRole(Long emisorId, Long receptorId) {
        User emisor = userService.findById(emisorId);
        User receptor = userService.findById(receptorId);
        
        String emisorRol = emisor.getRol().getNombre();
        String receptorRol = receptor.getRol().getNombre();
        
        boolean validCombination = (emisorRol.equals("ROLE_CLIENTE") && receptorRol.equals("ROLE_REPARTIDOR")) ||
                                   (emisorRol.equals("ROLE_REPARTIDOR") && receptorRol.equals("ROLE_CLIENTE"));
                                   
        if (!validCombination) {
            throw new ConflictException("Los mensajes solo están permitidos entre clientes y repartidores.");
        }
    }
}
