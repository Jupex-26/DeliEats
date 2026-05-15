package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.DetalleCarrito;
import org.example.ordersservice.repositories.DetalleCarritoRepository;
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
class DetalleCarritoServiceImplTest {

    @Mock
    private DetalleCarritoRepository detalleCarritoRepository;

    @InjectMocks
    private DetalleCarritoServiceImpl detalleCarritoService;

    @Test
    void save() {
        DetalleCarrito detalle = new DetalleCarrito();
        when(detalleCarritoRepository.save(detalle)).thenReturn(detalle);

        DetalleCarrito result = detalleCarritoService.save(detalle);

        assertNotNull(result);
        verify(detalleCarritoRepository).save(detalle);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<DetalleCarrito> page = new PageImpl<>(List.of(new DetalleCarrito()));
        when(detalleCarritoRepository.findAll(pageable)).thenReturn(page);

        Page<DetalleCarrito> result = detalleCarritoService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(detalleCarritoRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        DetalleCarrito detalle = new DetalleCarrito();
        when(detalleCarritoRepository.findById(id)).thenReturn(Optional.of(detalle));

        DetalleCarrito result = detalleCarritoService.findById(id);

        assertNotNull(result);
        verify(detalleCarritoRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(detalleCarritoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> detalleCarritoService.findById(id));
        verify(detalleCarritoRepository).findById(id);
    }

    @Test
    void findByCarritoId() {
        Long carritoId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<DetalleCarrito> page = new PageImpl<>(List.of(new DetalleCarrito()));
        when(detalleCarritoRepository.findByCarritoId(carritoId, pageable)).thenReturn(page);

        Page<DetalleCarrito> result = detalleCarritoService.findByCarritoId(carritoId, pageable);

        assertFalse(result.isEmpty());
        verify(detalleCarritoRepository).findByCarritoId(carritoId, pageable);
    }

    @Test
    void update_Found() {
        Long id = 1L;
        DetalleCarrito existingDetalle = new DetalleCarrito();
        DetalleCarrito newDetalle = new DetalleCarrito();
        when(detalleCarritoRepository.findById(id)).thenReturn(Optional.of(existingDetalle));
        when(detalleCarritoRepository.save(any(DetalleCarrito.class))).thenReturn(newDetalle);

        DetalleCarrito result = detalleCarritoService.update(id, newDetalle);

        assertNotNull(result);
        verify(detalleCarritoRepository).findById(id);
        verify(detalleCarritoRepository).save(any(DetalleCarrito.class));
    }

    @Test
    void update_NotFound() {
        Long id = 1L;
        DetalleCarrito newDetalle = new DetalleCarrito();
        when(detalleCarritoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> detalleCarritoService.update(id, newDetalle));
        verify(detalleCarritoRepository).findById(id);
        verify(detalleCarritoRepository, never()).save(any(DetalleCarrito.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(detalleCarritoRepository).deleteById(id);

        detalleCarritoService.deleteById(id);

        verify(detalleCarritoRepository).deleteById(id);
    }

    @Test
    void deleteByCarritoId() {
        Long carritoId = 1L;
        doNothing().when(detalleCarritoRepository).deleteByCarritoId(carritoId);

        detalleCarritoService.deleteByCarritoId(carritoId);

        verify(detalleCarritoRepository).deleteByCarritoId(carritoId);
    }
}
