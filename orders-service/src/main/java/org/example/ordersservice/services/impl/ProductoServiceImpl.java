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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final EmpresaService empresaService;

    private final String UPLOAD_DIR = "uploads";

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }
    
    @Override
    public Producto saveWithFoto(Producto producto, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            producto.setFoto(saveFile(file));
        }
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
        producto.setFoto(existingProducto.getFoto()); // Mantener foto existente si no se sube una nueva
        return productoRepository.save(producto);
    }

    @Override
    public Producto updateWithFoto(Long id, Producto producto, MultipartFile file) {
        Producto existingProducto = findById(id);
        
        producto.setId(existingProducto.getId());
        producto.setEmpresa(empresaService.findById(producto.getEmpresa().getId()));
        
        if (file != null && !file.isEmpty()) {
            // Delete old file if exists
            if (existingProducto.getFoto() != null) {
                Path fotoAnterior = Paths.get(UPLOAD_DIR).resolve(existingProducto.getFoto());
                try {
                    Files.deleteIfExists(fotoAnterior);
                } catch (IOException e) {
                    // Log error but continue
                }
            }
            producto.setFoto(saveFile(file));
        } else {
            producto.setFoto(existingProducto.getFoto());
        }
        
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

    private String saveFile(MultipartFile archivo) {
        try {
            Path rootPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Path filePath = rootPath.resolve(nombreUnico);

            Files.copy(archivo.getInputStream(), filePath);
            return nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("Error crítico al guardar la foto: " + e.getMessage());
        }
    }
}
