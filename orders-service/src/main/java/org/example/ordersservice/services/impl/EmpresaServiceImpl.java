package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.repositories.EmpresaRepository;
import org.example.ordersservice.services.EmpresaService;
import org.example.ordersservice.services.RolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        
        if (empresa.hasAperturas()) {
            empresa.getAperturas().forEach(a -> a.setEmpresa(empresa));
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
        empresa.setFoto(existingEmpresa.getFoto());

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
        
        if (empresa.hasAperturas()) {
            List<Apertura> existingAperturas = existingEmpresa.hasAperturas() ? existingEmpresa.getAperturas() : new ArrayList<>();
            
            for (Apertura newApertura : empresa.getAperturas()) {
                Optional<Apertura> matchOpt = existingAperturas.stream()
                        .filter(a -> a.getDia() == newApertura.getDia())
                        .findFirst();
                
                if (matchOpt.isPresent()) {
                    Apertura match = matchOpt.get();
                    match.setHoraApertura(newApertura.getHoraApertura());
                    match.setHoraCierre(newApertura.getHoraCierre());
                } else {
                    newApertura.setEmpresa(existingEmpresa);
                    existingAperturas.add(newApertura);
                }
            }
            empresa.setAperturas(existingAperturas);
        } else {
            empresa.setAperturas(existingEmpresa.getAperturas());
        }

        return empresaRepository.save(empresa);
    }

    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }
}
