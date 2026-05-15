package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.models.Producto;
import org.example.ordersservice.repositories.ProductoRepository;
import org.example.ordersservice.services.EmpresaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);

        producto = new Producto();
        producto.setId(1L);
        producto.setEmpresa(empresa);
    }

    @Test
    void save() {
        when(productoRepository.save(producto)).thenReturn(producto);
        Producto result = productoService.save(producto);
        assertNotNull(result);
        verify(productoRepository).save(producto);
    }

    @Test
    void saveWithFoto() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.saveWithFoto(producto, file);

        assertNotNull(result);
        assertNotNull(result.getFoto());
        verify(productoRepository).save(any(Producto.class));
        
        // Cleanup the created file
        Files.deleteIfExists(Path.of("uploads", result.getFoto()));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Producto> page = new PageImpl<>(List.of(producto));
        when(productoRepository.findAll(pageable)).thenReturn(page);

        Page<Producto> result = productoService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(productoRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        Producto result = productoService.findById(id);
        assertNotNull(result);
        verify(productoRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productoService.findById(id));
        verify(productoRepository).findById(id);
    }

    @Test
    void findByEmpresaId() {
        Long empresaId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<Producto> page = new PageImpl<>(List.of(producto));
        when(empresaService.findById(empresaId)).thenReturn(empresa);
        when(productoRepository.findByEmpresaId(empresaId, pageable)).thenReturn(page);

        Page<Producto> result = productoService.findByEmpresaId(empresaId, pageable);

        assertFalse(result.isEmpty());
        verify(productoRepository).findByEmpresaId(empresaId, pageable);
    }

    @Test
    void update_Success() {
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(empresaService.findById(anyLong())).thenReturn(empresa);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.update(id, producto);

        assertNotNull(result);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void updateWithFoto_NewFoto() throws IOException {
        Long id = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "new.jpg", "image/jpeg", "new data".getBytes());
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(empresaService.findById(anyLong())).thenReturn(empresa);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto result = productoService.updateWithFoto(id, producto, file);

        assertNotNull(result);
        assertNotNull(result.getFoto());
        assertNotEquals(producto.getFoto(), result.getFoto());
        verify(productoRepository).save(any(Producto.class));

        Files.deleteIfExists(Path.of("uploads", result.getFoto()));
    }
    
    @Test
    void updateWithFoto_NoNewFoto() {
        Long id = 1L;
        producto.setFoto("existing.jpg");
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(empresaService.findById(anyLong())).thenReturn(empresa);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.updateWithFoto(id, producto, null);

        assertNotNull(result);
        assertEquals("existing.jpg", result.getFoto());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(productoRepository).deleteById(id);
        productoService.deleteById(id);
        verify(productoRepository).deleteById(id);
    }

    @Test
    void updateStock() {
        Long id = 1L;
        int newStock = 50;
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        productoService.updateStock(id, newStock);

        assertEquals(newStock, producto.getCantidad());
        verify(productoRepository).save(producto);
    }
}
