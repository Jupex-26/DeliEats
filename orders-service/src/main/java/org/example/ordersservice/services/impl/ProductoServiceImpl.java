package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.models.Producto;
import org.example.ordersservice.repositories.ProductoRepository;
import org.example.ordersservice.services.EmpresaService;
import org.example.ordersservice.services.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final EmpresaService empresaService;

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
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable) {
        return Page.empty(pageable);
    }

    @Override
    public Page<Producto> findByEmpresaId(Long empresaId, Pageable pageable) {
        Empresa empresa = empresaService.findById(empresaId);
        return productoRepository.findByEmpresaId(empresa.getId(), pageable);
    }

    @Override
    public Page<Producto> findByNombreContaining(String nombre, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Override
    public Producto update(Long id, Producto producto) {
        Producto existingProducto = findById(id);

        producto.setEmpresa(empresaService.findById(producto.getEmpresa().getId()));

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
