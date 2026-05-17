package org.example.ordersservice.services;

import org.example.ordersservice.models.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpresaService {

    Empresa save(Empresa empresa);

    Page<Empresa> findAll(Pageable pageable);

    Page<Empresa> findAll(String search, Pageable pageable);

    Empresa findById(Long id);

    Empresa update(Long id, Empresa empresa);

    void deleteById(Long id);

}
