package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.ConflictException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.models.User;
import org.example.ordersservice.repositories.MensajeRepository;
import org.example.ordersservice.services.RepartidorService;
import org.example.ordersservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MensajeServiceImplTest {

    @Mock
    private MensajeRepository mensajeRepository;
    @Mock
    private UserService userService;
    @Mock
    private RepartidorService repartidorService;

    @InjectMocks
    private MensajeServiceImpl mensajeService;

    private User cliente;
    private User repartidor;
    private Mensaje mensaje;

    @BeforeEach
    void setUp() {
        Rol rolCliente = new Rol(1L, "ROLE_CLIENTE");

        cliente = new User();
        cliente.setId(1L);
        cliente.setRol(rolCliente);

        repartidor = new User();
        repartidor.setId(2L);
        repartidor.setRol(rolCliente); // Todos son ROLE_CLIENTE ahora

        mensaje = new Mensaje();
        mensaje.setEmisor(cliente);
        mensaje.setReceptor(repartidor);
        mensaje.setFecha(LocalDateTime.now());
    }

    @Test
    void save_Success() {
        when(userService.findById(1L)).thenReturn(cliente);
        when(userService.findById(2L)).thenReturn(repartidor);
        when(repartidorService.isRepartidor(1L)).thenReturn(false);
        when(repartidorService.isRepartidor(2L)).thenReturn(true);
        when(mensajeRepository.save(mensaje)).thenReturn(mensaje);

        Mensaje result = mensajeService.save(mensaje);

        assertNotNull(result);
        // validateUsersRole llama a findById dos veces, y luego el save del servicio llama a findById dos veces más.
        verify(userService, times(4)).findById(anyLong()); 
        verify(mensajeRepository).save(mensaje);
    }

    @Test
    void save_InvalidRoles() {
        User otroCliente = new User();
        otroCliente.setId(3L);
        otroCliente.setRol(new Rol(1L, "ROLE_CLIENTE"));
        mensaje.setReceptor(otroCliente);

        when(userService.findById(1L)).thenReturn(cliente);
        when(userService.findById(3L)).thenReturn(otroCliente);
        
        // Ninguno es repartidor
        when(repartidorService.isRepartidor(1L)).thenReturn(false);
        when(repartidorService.isRepartidor(3L)).thenReturn(false);

        assertThrows(ConflictException.class, () -> mensajeService.save(mensaje));
        verify(mensajeRepository, never()).save(any(Mensaje.class));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Mensaje> page = new PageImpl<>(List.of(mensaje));
        when(mensajeRepository.findAll(pageable)).thenReturn(page);

        Page<Mensaje> result = mensajeService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(mensajeRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(mensajeRepository.findById(id)).thenReturn(Optional.of(mensaje));

        Mensaje result = mensajeService.findById(id);

        assertNotNull(result);
        verify(mensajeRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(mensajeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> mensajeService.findById(id));
        verify(mensajeRepository).findById(id);
    }

    @Test
    void findChat() {
        Long id1 = 1L;
        Long id2 = 2L;
        Pageable pageable = Pageable.unpaged();
        Page<Mensaje> page = new PageImpl<>(List.of(mensaje));

        when(userService.findById(id1)).thenReturn(cliente);
        when(userService.findById(id2)).thenReturn(repartidor);
        when(repartidorService.isRepartidor(1L)).thenReturn(false);
        when(repartidorService.isRepartidor(2L)).thenReturn(true);
        when(mensajeRepository.findByEmisor_IdAndReceptor_IdOrEmisor_IdAndReceptor_IdOrderByFechaAsc(id1, id2, id2, id1, pageable))
                .thenReturn(page);

        Page<Mensaje> result = mensajeService.findChat(id1, id2, pageable);

        assertFalse(result.isEmpty());
        // validateUsersRole llama a findById dos veces
        verify(userService, times(2)).findById(anyLong());
        verify(mensajeRepository).findByEmisor_IdAndReceptor_IdOrEmisor_IdAndReceptor_IdOrderByFechaAsc(id1, id2, id2, id1, pageable);
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(mensajeRepository).deleteById(id);

        mensajeService.deleteById(id);

        verify(mensajeRepository).deleteById(id);
    }

    @Test
    void markAsRead() {
        Long id = 1L;
        mensaje.setLeido(false);
        when(mensajeRepository.findById(id)).thenReturn(Optional.of(mensaje));
        when(mensajeRepository.save(mensaje)).thenReturn(mensaje);

        mensajeService.markAsRead(id);

        assertTrue(mensaje.isLeido());
        verify(mensajeRepository).findById(id);
        verify(mensajeRepository).save(mensaje);
    }
}
