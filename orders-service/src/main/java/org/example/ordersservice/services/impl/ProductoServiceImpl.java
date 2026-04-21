package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Producto;
import org.example.ordersservice.repositories.ProductoRepository;
import org.example.ordersservice.services.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;

    @Override
    public Producto save(Producto producto) {
        return null;
    }

    @Override
    public List<Producto> findAll() {
        return List.of();
    }

    @Override
    public Producto findById(Long id) {
        return null;
    }

    @Override
    public List<Producto> findByCategoriaId(Long categoriaId) {
        return List.of();
    }

    @Override
    public List<Producto> findByNombreContaining(String nombre) {
        return List.of();
    }

    @Override
    public Producto update(Long id, Producto producto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void updateStock(Long id, Integer cantidad) {

    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
