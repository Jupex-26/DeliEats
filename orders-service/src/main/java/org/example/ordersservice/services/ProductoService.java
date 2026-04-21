package org.example.ordersservice.services;

import org.example.ordersservice.models.Producto;

import java.util.List;

public interface ProductoService {

    Producto save(Producto producto);

    List<Producto> findAll();

    Producto findById(Long id);

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContaining(String nombre);

    Producto update(Long id, Producto producto);

    void deleteById(Long id);

    void updateStock(Long id, Integer cantidad);

    boolean existsById(Long id);
}