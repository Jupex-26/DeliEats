package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Producto;
import org.example.ordersservice.repositories.ProductoRepository;
import org.example.ordersservice.services.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Page<Producto> findAll(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable) {
        // En tu modelo, el producto está asociado a una Empresa, no a una Categoría explícita
        // Si no existe la relación Categoria, devolvemos un Page vacío por ahora
        return Page.empty(pageable);
    }

    @Override
    public Page<Producto> findByNombreContaining(String nombre, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Override
    public Producto update(Long id, Producto producto) {
        Producto existingProducto = findById(id);
        producto.setId(existingProducto.getId());
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public void updateStock(Long id, Integer cantidad) {
        Producto producto = findById(id);
        producto.setCantidad(cantidad);
        productoRepository.save(producto);
    }

    @Override
    public boolean existsById(Long id) {
        return productoRepository.existsById(id);
    }
}
