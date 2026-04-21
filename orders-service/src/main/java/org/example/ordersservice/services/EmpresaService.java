package org.example.ordersservice.services;

import org.example.ordersservice.models.Empresa;

import java.util.List;

public interface EmpresaService {

    Empresa save(Empresa empresa);

    List<Empresa> findAll();

    Empresa findById(Long id);

    Empresa update(Long id, Empresa empresa);

    void deleteById(Long id);

}