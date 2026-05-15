package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.*;
import org.example.ordersservice.repositories.PedidoRepository;
import org.example.ordersservice.services.ClienteService;
import org.example.ordersservice.services.EmpresaService;
import org.example.ordersservice.services.EstadoService;
import org.example.ordersservice.services.RepartidorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private EstadoService estadoService;
    @Mock
    private RepartidorService repartidorService;
    @Mock
    private EmpresaService empresaService;
    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private Empresa empresa;
    private Cliente cliente;
    private Estado estado;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);

        cliente = new Cliente();
        cliente.setId(1L);

        estado = new Estado();
        estado.setId(1L);
        estado.setNombre("PENDIENTE");

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEmpresa(empresa);
        pedido.setCliente(cliente);
        pedido.setEstado(estado);
        pedido.setDetalles(new ArrayList<>());
    }

    @Test
    void save() {
        when(empresaService.findById(anyLong())).thenReturn(empresa);
        when(estadoService.findById(anyLong())).thenReturn(estado);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.save(pedido);

        assertNotNull(result);
        assertNotNull(result.getFechaCompra());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Pedido> page = new PageImpl<>(List.of(pedido));
        when(pedidoRepository.findAll(pageable)).thenReturn(page);

        Page<Pedido> result = pedidoService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(pedidoRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        Pedido result = pedidoService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(pedidoRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pedidoService.findById(id));
        verify(pedidoRepository).findById(id);
    }

    @Test
    void findByClienteId() {
        Long clienteId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<Pedido> page = new PageImpl<>(List.of(pedido));
        when(clienteService.findById(clienteId)).thenReturn(cliente);
        when(pedidoRepository.findAllByClienteId(clienteId, pageable)).thenReturn(page);

        Page<Pedido> result = pedidoService.findByClienteId(clienteId, pageable);

        assertFalse(result.isEmpty());
        verify(pedidoRepository).findAllByClienteId(clienteId, pageable);
    }

    @Test
    void findByEmpresaId() {
        Long empresaId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<Pedido> page = new PageImpl<>(List.of(pedido));
        when(empresaService.findById(empresaId)).thenReturn(empresa);
        when(pedidoRepository.findAllByEmpresaId(empresaId, pageable)).thenReturn(page);

        Page<Pedido> result = pedidoService.findByEmpresaId(empresaId, pageable);

        assertFalse(result.isEmpty());
        verify(pedidoRepository).findAllByEmpresaId(empresaId, pageable);
    }

    @Test
    void findByEmpresaIdMesActual() {
        Long empresaId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<Pedido> page = new PageImpl<>(List.of(pedido));
        LocalDateTime inicioMes = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime finMes = YearMonth.now().atEndOfMonth().atTime(23, 59, 59, 999999999);

        when(empresaService.findById(empresaId)).thenReturn(empresa);
        when(pedidoRepository.findAllByEmpresaIdAndFechaCompraBetween(eq(empresaId), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(page);

        Page<Pedido> result = pedidoService.findByEmpresaIdMesActual(empresaId, pageable);

        assertFalse(result.isEmpty());
        verify(pedidoRepository).findAllByEmpresaIdAndFechaCompraBetween(eq(empresaId), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable));
    }

    @Test
    void findByEmpresaIdAndMesAndAnio() {
        Long empresaId = 1L;
        int mes = 5;
        int anio = 2026;
        Pageable pageable = Pageable.unpaged();
        Page<Pedido> page = new PageImpl<>(List.of(pedido));

        when(empresaService.findById(empresaId)).thenReturn(empresa);
        when(pedidoRepository.findAllByEmpresaIdAndFechaCompraBetween(eq(empresaId), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(page);

        Page<Pedido> result = pedidoService.findByEmpresaIdAndMesAndAnio(empresaId, mes, anio, pageable);

        assertFalse(result.isEmpty());
        verify(pedidoRepository).findAllByEmpresaIdAndFechaCompraBetween(eq(empresaId), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(pedidoRepository).deleteById(id);

        pedidoService.deleteById(id);

        verify(pedidoRepository).deleteById(id);
    }

    @Test
    void update() {
        Long id = 1L;
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(empresaService.findById(anyLong())).thenReturn(empresa);
        when(estadoService.findById(anyLong())).thenReturn(estado);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.update(id, pedido);

        assertNotNull(result);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void updateEstado() {
        Long id = 1L;
        Long estadoId = 2L;
        Estado nuevoEstado = new Estado();
        nuevoEstado.setId(estadoId);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(estadoService.findById(estadoId)).thenReturn(nuevoEstado);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.updateEstado(id, estadoId);

        assertNotNull(result);
        assertEquals(nuevoEstado, result.getEstado());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void cancelarPedido() {
        Long id = 1L;
        Estado estadoCancelado = new Estado();
        estadoCancelado.setNombre("CANCELADO");

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(estadoService.findByNombre("CANCELADO")).thenReturn(estadoCancelado);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.cancelarPedido(id);

        assertNotNull(result);
        assertEquals(estadoCancelado, result.getEstado());
        verify(pedidoRepository).save(pedido);
    }
}
