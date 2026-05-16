package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.repositories.RepartidorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepartidorServiceImplTest {

    @Mock
    private RepartidorRepository repartidorRepository;

    @InjectMocks
    private RepartidorServiceImpl repartidorService;

    private Repartidor repartidor;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);

        repartidor = new Repartidor();
        repartidor.setId(10L);
        repartidor.setCliente(cliente);
        repartidor.setDisponible(true);
        repartidor.setAprobado(true);
    }

    @Test
    void save_Success() {
        when(repartidorRepository.save(repartidor)).thenReturn(repartidor);
        Repartidor result = repartidorService.save(repartidor);
        assertNotNull(result);
        verify(repartidorRepository).save(repartidor);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Repartidor> page = new PageImpl<>(List.of(repartidor));
        when(repartidorRepository.findAll(pageable)).thenReturn(page);

        Page<Repartidor> result = repartidorService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(repartidorRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 10L;
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidor));
        Repartidor result = repartidorService.findById(id);
        assertNotNull(result);
        verify(repartidorRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 10L;
        when(repartidorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> repartidorService.findById(id));
        verify(repartidorRepository).findById(id);
    }

    @Test
    void findByClienteId_Found() {
        Long clienteId = 1L;
        when(repartidorRepository.findByClienteId(clienteId)).thenReturn(Optional.of(repartidor));
        Repartidor result = repartidorService.findByClienteId(clienteId);
        assertNotNull(result);
        verify(repartidorRepository).findByClienteId(clienteId);
    }

    @Test
    void findByClienteId_NotFound() {
        Long clienteId = 1L;
        when(repartidorRepository.findByClienteId(clienteId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> repartidorService.findByClienteId(clienteId));
        verify(repartidorRepository).findByClienteId(clienteId);
    }

    @Test
    void findByDisponible() {
        Pageable pageable = Pageable.unpaged();
        Page<Repartidor> page = new PageImpl<>(List.of(repartidor));
        when(repartidorRepository.findByDisponible(true, pageable)).thenReturn(page);

        Page<Repartidor> result = repartidorService.findByDisponible(true, pageable);

        assertFalse(result.isEmpty());
        verify(repartidorRepository).findByDisponible(true, pageable);
    }

    @Test
    void findByAprobado() {
        Pageable pageable = Pageable.unpaged();
        Page<Repartidor> page = new PageImpl<>(List.of(repartidor));
        when(repartidorRepository.findByAprobado(true, pageable)).thenReturn(page);

        Page<Repartidor> result = repartidorService.findByAprobado(true, pageable);

        assertFalse(result.isEmpty());
        verify(repartidorRepository).findByAprobado(true, pageable);
    }

    @Test
    void update() {
        Long id = 10L;
        Repartidor existingRepartidor = new Repartidor();
        existingRepartidor.setId(id);
        existingRepartidor.setDisponible(false);
        
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(existingRepartidor));
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidor);

        Repartidor result = repartidorService.update(id, repartidor);

        assertNotNull(result);
        verify(repartidorRepository).save(any(Repartidor.class));
    }

    @Test
    void deleteById() {
        Long id = 10L;
        doNothing().when(repartidorRepository).deleteById(id);
        repartidorService.deleteById(id);
        verify(repartidorRepository).deleteById(id);
    }

    @Test
    void updateDisponibilidad() {
        Long clienteId = 1L;
        when(repartidorRepository.findByClienteId(clienteId)).thenReturn(Optional.of(repartidor));
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidor);

        repartidorService.updateDisponibilidad(clienteId, false);

        assertFalse(repartidor.getDisponible());
        verify(repartidorRepository).save(repartidor);
    }

    @Test
    void createFromCliente_New() {
        when(repartidorRepository.existsByClienteId(cliente.getId())).thenReturn(false);
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidor);
        
        repartidorService.createFromCliente(cliente);

        verify(repartidorRepository).save(any(Repartidor.class));
    }

    @Test
    void createFromCliente_AlreadyExists() {
        when(repartidorRepository.existsByClienteId(cliente.getId())).thenReturn(true);
        
        repartidorService.createFromCliente(cliente);

        verify(repartidorRepository, never()).save(any(Repartidor.class));
    }

    @Test
    void aprobarRepartidor() {
        Long id = 10L;
        repartidor.setAprobado(false);
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidor));
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidor);

        repartidorService.aprobarRepartidor(id, true);

        assertTrue(repartidor.getAprobado());
        verify(repartidorRepository).save(repartidor);
    }

    @Test
    void isRepartidor_True() {
        Long clienteId = 1L;
        when(repartidorRepository.findByClienteId(clienteId)).thenReturn(Optional.of(repartidor));
        assertTrue(repartidorService.isRepartidor(clienteId));
    }

    @Test
    void isRepartidor_False_NotApproved() {
        Long clienteId = 1L;
        repartidor.setAprobado(false);
        when(repartidorRepository.findByClienteId(clienteId)).thenReturn(Optional.of(repartidor));
        assertFalse(repartidorService.isRepartidor(clienteId));
    }

    @Test
    void isRepartidor_False_NotFound() {
        Long clienteId = 1L;
        when(repartidorRepository.findByClienteId(clienteId)).thenReturn(Optional.empty());
        assertFalse(repartidorService.isRepartidor(clienteId));
    }
}
