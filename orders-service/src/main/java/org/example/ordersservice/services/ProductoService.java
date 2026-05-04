package org.example.ordersservice.services;

import org.example.ordersservice.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {

    Producto save(Producto producto);

    Page<Producto> findAll(Pageable pageable);

    Producto findById(Long id);

    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);

    Page<Producto> findByNombreContaining(String nombre, Pageable pageable);

    Producto update(Long id, Producto producto);

    void deleteById(Long id);

    void updateStock(Long id, Integer cantidad);

    boolean existsById(Long id);
}
