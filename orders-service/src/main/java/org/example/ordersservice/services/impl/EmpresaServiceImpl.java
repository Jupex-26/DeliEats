package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.repositories.EmpresaRepository;
import org.example.ordersservice.services.EmpresaService;
import org.example.ordersservice.services.RolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolService rolService;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    @Override
    public Empresa save(Empresa empresa) {
        String rawPassword = empresa.getPassword();

        if (Objects.isNull(rawPassword) || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException("La contraseña no cumple con el formato de seguridad requerido");
        }

        empresa.setPassword(passwordEncoder.encode(rawPassword));

        empresa.setRol(rolService.findByNombre("ROLE_EMPRESA"));

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

        String rawPassword = empresa.getPassword();

        if (Objects.nonNull(rawPassword) && !rawPassword.isEmpty()) {

            if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
                throw new IllegalArgumentException("La nueva contraseña no cumple con el formato de seguridad");
            }

            empresa.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            empresa.setPassword(existingEmpresa.getPassword());
        }

        empresa.setRol(rolService.findByNombre("ROLE_EMPRESA"));

        return empresaRepository.save(empresa);
    }

    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }
}
