package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.repositories.EstadoRepository;
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
class EstadoServiceImplTest {

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoServiceImpl estadoService;

    @Test
    void save() {
        Estado estado = new Estado();
        when(estadoRepository.save(estado)).thenReturn(estado);

        Estado result = estadoService.save(estado);

        assertNotNull(result);
        verify(estadoRepository).save(estado);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Estado> page = new PageImpl<>(List.of(new Estado()));
        when(estadoRepository.findAll(pageable)).thenReturn(page);

        Page<Estado> result = estadoService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(estadoRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        Estado estado = new Estado();
        when(estadoRepository.findById(id)).thenReturn(Optional.of(estado));

        Estado result = estadoService.findById(id);

        assertNotNull(result);
        verify(estadoRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(estadoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> estadoService.findById(id));
        verify(estadoRepository).findById(id);
    }

    @Test
    void findByNombre_Found() {
        String nombre = "PENDIENTE";
        Estado estado = new Estado();
        when(estadoRepository.findByNombre(nombre)).thenReturn(Optional.of(estado));

        Estado result = estadoService.findByNombre(nombre);

        assertNotNull(result);
        verify(estadoRepository).findByNombre(nombre);
    }

    @Test
    void findByNombre_NotFound() {
        String nombre = "NO_EXISTE";
        when(estadoRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> estadoService.findByNombre(nombre));
        verify(estadoRepository).findByNombre(nombre);
    }

    @Test
    void update_Found() {
        Long id = 1L;
        Estado existingEstado = new Estado();
        Estado newEstado = new Estado();
        when(estadoRepository.findById(id)).thenReturn(Optional.of(existingEstado));
        when(estadoRepository.save(any(Estado.class))).thenReturn(newEstado);

        Estado result = estadoService.update(id, newEstado);

        assertNotNull(result);
        verify(estadoRepository).findById(id);
        verify(estadoRepository).save(any(Estado.class));
    }

    @Test
    void update_NotFound() {
        Long id = 1L;
        Estado newEstado = new Estado();
        when(estadoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> estadoService.update(id, newEstado));
        verify(estadoRepository).findById(id);
        verify(estadoRepository, never()).save(any(Estado.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(estadoRepository).deleteById(id);

        estadoService.deleteById(id);

        verify(estadoRepository).deleteById(id);
    }
}
