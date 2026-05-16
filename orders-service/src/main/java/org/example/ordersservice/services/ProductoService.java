package org.example.ordersservice.services;

import org.example.ordersservice.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductoService {

    Producto save(Producto producto);
    
    Producto saveWithFoto(Producto producto, MultipartFile file);

    Page<Producto> findAll(Pageable pageable);

    Producto findById(Long id);

    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);
    
    Page<Producto> findByEmpresaId(Long empresaId, Pageable pageable);

    Producto update(Long id, Producto producto);
    
    Producto updateWithFoto(Long id, Producto producto, MultipartFile file);

    void deleteById(Long id);

    void updateStock(Long id, Integer cantidad);

}
