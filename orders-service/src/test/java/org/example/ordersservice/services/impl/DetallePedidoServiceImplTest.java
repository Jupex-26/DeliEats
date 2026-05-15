package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.DetallePedido;
import org.example.ordersservice.repositories.DetallePedidoRepository;
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
class DetallePedidoServiceImplTest {

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    @InjectMocks
    private DetallePedidoServiceImpl detallePedidoService;

    @Test
    void save() {
        DetallePedido detalle = new DetallePedido();
        when(detallePedidoRepository.save(detalle)).thenReturn(detalle);

        DetallePedido result = detallePedidoService.save(detalle);

        assertNotNull(result);
        verify(detallePedidoRepository).save(detalle);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<DetallePedido> page = new PageImpl<>(List.of(new DetallePedido()));
        when(detallePedidoRepository.findAll(pageable)).thenReturn(page);

        Page<DetallePedido> result = detallePedidoService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(detallePedidoRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        DetallePedido detalle = new DetallePedido();
        when(detallePedidoRepository.findById(id)).thenReturn(Optional.of(detalle));

        DetallePedido result = detallePedidoService.findById(id);

        assertNotNull(result);
        verify(detallePedidoRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(detallePedidoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> detallePedidoService.findById(id));
        verify(detallePedidoRepository).findById(id);
    }

    @Test
    void findByPedidoId() {
        Long pedidoId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<DetallePedido> page = new PageImpl<>(List.of(new DetallePedido()));
        when(detallePedidoRepository.findByPedidoId(pedidoId, pageable)).thenReturn(page);

        Page<DetallePedido> result = detallePedidoService.findByPedidoId(pedidoId, pageable);

        assertFalse(result.isEmpty());
        verify(detallePedidoRepository).findByPedidoId(pedidoId, pageable);
    }

    @Test
    void update_Found() {
        Long id = 1L;
        DetallePedido existingDetalle = new DetallePedido();
        DetallePedido newDetalle = new DetallePedido();
        when(detallePedidoRepository.findById(id)).thenReturn(Optional.of(existingDetalle));
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(newDetalle);

        DetallePedido result = detallePedidoService.update(id, newDetalle);

        assertNotNull(result);
        verify(detallePedidoRepository).findById(id);
        verify(detallePedidoRepository).save(any(DetallePedido.class));
    }

    @Test
    void update_NotFound() {
        Long id = 1L;
        DetallePedido newDetalle = new DetallePedido();
        when(detallePedidoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> detallePedidoService.update(id, newDetalle));
        verify(detallePedidoRepository).findById(id);
        verify(detallePedidoRepository, never()).save(any(DetallePedido.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(detallePedidoRepository).deleteById(id);

        detallePedidoService.deleteById(id);

        verify(detallePedidoRepository).deleteById(id);
    }

    @Test
    void deleteByPedidoId() {
        Long pedidoId = 1L;
        doNothing().when(detallePedidoRepository).deleteByPedidoId(pedidoId);

        detallePedidoService.deleteByPedidoId(pedidoId);

        verify(detallePedidoRepository).deleteByPedidoId(pedidoId);
    }
}
