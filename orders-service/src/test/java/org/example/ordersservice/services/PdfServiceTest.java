package org.example.ordersservice.services;

import org.example.ordersservice.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @InjectMocks
    private PdfService pdfService;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        Empresa empresa = new Empresa();
        empresa.setNombre("Test Empresa");
        empresa.setCorreoContacto("contacto@empresa.com");
        empresa.setTelefonoContacto("123456789");
        empresa.setDireccion("Calle Falsa 123");

        Cliente cliente = new Cliente();
        cliente.setNombre("Test Cliente");
        cliente.setDireccion("Avenida Siempre Viva 456");
        cliente.setTelefono(987654321L);

        Producto producto1 = new Producto();
        producto1.setNombre("Producto 1");
        producto1.setPrecio(new BigDecimal("10.00"));

        DetallePedido detalle1 = new DetallePedido();
        detalle1.setProducto(producto1);
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(producto1.getPrecio());

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEmpresa(empresa);
        pedido.setCliente(cliente);
        pedido.setFechaCompra(LocalDateTime.now());
        pedido.setDetalles(List.of(detalle1));
        pedido.setPrecio(new BigDecimal("20.00"));
    }

    @Test
    void generarFactura() {
        byte[] pdfBytes = pdfService.generarFactura(pedido);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        // A simple check to see if it starts with PDF magic number
        assertTrue(new String(pdfBytes, 0, 4).equals("%PDF"));
    }
}
