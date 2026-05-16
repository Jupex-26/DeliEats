package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.*;
import org.example.ordersservice.repositories.CarritoRepository;
import org.example.ordersservice.services.ClienteService;
import org.example.ordersservice.services.DetalleCarritoService;
import org.example.ordersservice.services.EmpresaService;
import org.example.ordersservice.services.ProductoService;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private ProductoService productoService;
    @Mock
    private DetalleCarritoService detalleCarritoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito carrito;
    private Producto producto1;
    private Producto producto2;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(100L); // Set a client ID

        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setDetalles(new ArrayList<>());
        carrito.setCliente(cliente); // Set the client for the carrito

        producto1 = new Producto();
        producto1.setId(10L);
        producto1.setNombre("Producto 1");
        producto1.setPrecio(new BigDecimal("10.00"));
        producto1.setCantidad(5);

        producto2 = new Producto();
        producto2.setId(20L);
        producto2.setNombre("Producto 2");
        producto2.setPrecio(new BigDecimal("5.00"));
        producto2.setCantidad(10);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Carrito> page = new PageImpl<>(List.of(carrito));
        when(carritoRepository.findAll(pageable)).thenReturn(page);

        Page<Carrito> result = carritoService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(carritoRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(carritoRepository.findById(id)).thenReturn(Optional.of(carrito));

        Carrito result = carritoService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(carritoRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(carritoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carritoService.findById(id));
        verify(carritoRepository).findById(id);
    }

    @Test
    void findByClienteId_Found() {
        Long clienteId = 100L;
        when(carritoRepository.findByClienteId(clienteId)).thenReturn(Optional.of(carrito));

        Carrito result = carritoService.findByClienteId(clienteId);

        assertNotNull(result);
        verify(carritoRepository).findByClienteId(clienteId);
    }

    @Test
    void findByClienteId_NotFound() {
        Long clienteId = 100L;
        when(carritoRepository.findByClienteId(clienteId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carritoService.findByClienteId(clienteId));
        verify(carritoRepository).findByClienteId(clienteId);
    }

    @Test
    void create() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);

        // Apertura que cubre todo el día de hoy
        Apertura apertura = new Apertura();
        apertura.setDia(Dia.from(LocalDateTime.now().getDayOfWeek()));
        apertura.setHoraApertura(LocalTime.of(0, 0));
        apertura.setHoraCierre(LocalTime.of(23, 59));
        empresa.setAperturas(List.of(apertura));

        producto1.setEmpresa(empresa);

        DetalleCarrito detalle = new DetalleCarrito();
        detalle.setProducto(producto1);
        detalle.setCantidad(1);
        carrito.getDetalles().add(detalle);

        when(clienteService.findById(anyLong())).thenReturn(cliente);
        when(empresaService.findById(anyLong())).thenReturn(empresa);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        Carrito result = carritoService.create(carrito);

        assertNotNull(result);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(carritoRepository).deleteById(id);

        carritoService.deleteById(id);

        verify(carritoRepository).deleteById(id);
    }

    @Test
    void addProducto_NewItem() {
        Long carritoId = 1L;
        Long productoId = 10L;
        Integer cantidad = 2;

        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(productoService.findById(productoId)).thenReturn(producto1);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        Carrito result = carritoService.addProducto(carritoId, productoId, cantidad);

        assertNotNull(result);
        assertEquals(1, result.getDetalles().size());
        assertEquals(cantidad, result.getDetalles().getFirst().getCantidad());
        verify(carritoRepository).findById(carritoId);
        verify(productoService).findById(productoId);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void addProducto_ExistingItem() {
        Long carritoId = 1L;
        Long productoId = 10L;
        int cantidad = 2;

        DetalleCarrito existingDetalle = new DetalleCarrito();
        existingDetalle.setProducto(producto1);
        existingDetalle.setCantidad(1);
        carrito.getDetalles().add(existingDetalle);

        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(productoService.findById(productoId)).thenReturn(producto1);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        Carrito result = carritoService.addProducto(carritoId, productoId, cantidad);

        assertNotNull(result);
        assertEquals(1, result.getDetalles().size());
        assertEquals(cantidad, result.getDetalles().getFirst().getCantidad()); // solo la nueva cantidad
        verify(carritoRepository).findById(carritoId);
        verify(productoService).findById(productoId);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void removeProducto_ExistingItem() {
        Long carritoId = 1L;
        Long productoId = 10L;

        DetalleCarrito existingDetalle = new DetalleCarrito();
        existingDetalle.setProducto(producto1);
        existingDetalle.setCantidad(1);
        carrito.getDetalles().add(existingDetalle);

        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        Carrito result = carritoService.removeProducto(carritoId, productoId);

        assertNotNull(result);
        assertTrue(result.getDetalles().isEmpty());
        verify(carritoRepository).findById(carritoId);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void removeProducto_NonExistingItem() {
        Long carritoId = 1L;
        Long productoId = 99L; // Non-existent product

        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        Carrito result = carritoService.removeProducto(carritoId, productoId);

        assertNotNull(result);
        assertTrue(result.getDetalles().isEmpty()); // Should still be empty as no item was added
        verify(carritoRepository).findById(carritoId);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void clearCarrito() {
        Long carritoId = 1L;
        carrito.getDetalles().add(new DetalleCarrito()); // Add some details

        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        doNothing().when(detalleCarritoService).deleteByCarritoId(carritoId);

        carritoService.clearCarrito(carritoId);

        verify(carritoRepository).findById(carritoId);
        verify(detalleCarritoService).deleteByCarritoId(carritoId);
    }

    @Test
    void calculateTotal() {
        DetalleCarrito detalle1 = new DetalleCarrito();
        detalle1.setProducto(producto1);
        detalle1.setCantidad(2);

        DetalleCarrito detalle2 = new DetalleCarrito();
        detalle2.setProducto(producto2);
        detalle2.setCantidad(3);

        carrito.getDetalles().add(detalle1);
        carrito.getDetalles().add(detalle2);

        when(carritoRepository.findById(carrito.getId())).thenReturn(Optional.of(carrito));

        BigDecimal total = carritoService.calculateTotal(carrito.getId());

        BigDecimal expectedTotal = new BigDecimal("10.00").multiply(new BigDecimal(2))
                .add(new BigDecimal("5.00").multiply(new BigDecimal(3)));
        assertEquals(expectedTotal, total);
        verify(carritoRepository).findById(carrito.getId());
    }
}
