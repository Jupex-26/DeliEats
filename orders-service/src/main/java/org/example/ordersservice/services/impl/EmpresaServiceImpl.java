package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.repositories.EmpresaRepository;
import org.example.ordersservice.services.EmpresaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Empresa save(Empresa empresa) {
        if (Objects.nonNull(empresa.getPassword())) {
            empresa.setPassword(passwordEncoder.encode(empresa.getPassword()));
        }
        return empresaRepository.save(empresa);
    }

    @Override
    public Page<Empresa> findAll(Pageable pageable) {
        return empresaRepository.findAll(pageable);
    }

    @Override
    public Empresa findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada con ID: " + id));
    }

    @Override
    public Empresa update(Long id, Empresa empresa) {
        Empresa existingEmpresa = findById(id);
        empresa.setId(existingEmpresa.getId());
        
        if (Objects.nonNull(empresa.getPassword()) && !empresa.getPassword().isEmpty()) {
            empresa.setPassword(passwordEncoder.encode(empresa.getPassword()));
        } else {
            empresa.setPassword(existingEmpresa.getPassword());
        }

        return empresaRepository.save(empresa);
    }

    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }
}
