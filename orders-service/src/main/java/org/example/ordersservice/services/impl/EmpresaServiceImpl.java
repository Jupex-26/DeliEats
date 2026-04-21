package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.repositories.EmpresaRepository;
import org.example.ordersservice.services.EmpresaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {
    private final EmpresaRepository empresaRepository;

    @Override
    public Empresa save(Empresa empresa) {
        return null;
    }

    @Override
    public List<Empresa> findAll() {
        return List.of();
    }

    @Override
    public Empresa findById(Long id) {
        return null;
    }

    @Override
    public Empresa update(Long id, Empresa empresa) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
