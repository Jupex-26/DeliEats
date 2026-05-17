package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.repositories.EmpresaRepository;
import org.example.ordersservice.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolService rolService;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    @Override
    public Empresa save(Empresa empresa) {
        if (userRepository.existsByEmail(empresa.getEmail())) {
            throw new EmailExistsException("El email " + empresa.getEmail() + " ya está registrado.");
        }

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
    public Page<Empresa> findAll(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return empresaRepository.findAll(pageable);
        }
        return empresaRepository.findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
    }

    @Override
    public Empresa findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa no encontrada con ID: " + id));
    }

    @Override
    public Empresa update(Long id, Empresa empresa) {
        // 1. Buscamos la empresa real de la base de datos (Entidad Gestionada)
        Empresa existingEmpresa = findById(id);

        // Validar Email
        if (!existingEmpresa.getEmail().equalsIgnoreCase(empresa.getEmail()) && userRepository.existsByEmail(empresa.getEmail())) {
            throw new EmailExistsException("El email " + empresa.getEmail() + " ya está en uso por otro usuario.");
        }

        // 2. Actualizamos los datos básicos de la empresa existente
        existingEmpresa.setNombre(empresa.getNombre());
        existingEmpresa.setEmail(empresa.getEmail());
        existingEmpresa.setDireccion(empresa.getDireccion());
        existingEmpresa.setTelefono(empresa.getTelefono());
        existingEmpresa.setDescripcion(empresa.getDescripcion());
        existingEmpresa.setCorreoContacto(empresa.getCorreoContacto());
        existingEmpresa.setTelefonoContacto(empresa.getTelefonoContacto());
        existingEmpresa.setTipoCocina(empresa.getTipoCocina());

        // Gestión de contraseña
        String rawPassword = empresa.getPassword();
        if (Objects.nonNull(rawPassword) && !rawPassword.isEmpty()) {
            if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
                throw new IllegalArgumentException("La nueva contraseña no cumple con el formato de seguridad");
            }
            existingEmpresa.setPassword(passwordEncoder.encode(rawPassword));
        }

        // 3. GESTIÓN DE APERTURAS (Modificando la lista gestionada)
        if (empresa.hasAperturas()) {
            List<Apertura> existingAperturas = existingEmpresa.getAperturas();

            List<Apertura> nuevasAperturas = new ArrayList<>();

            for (Apertura newApertura : empresa.getAperturas()) {
                Optional<Apertura> matchOpt = existingAperturas.stream()
                        .filter(a -> a.getDia() == newApertura.getDia())
                        .findFirst();

                if (matchOpt.isPresent()) {
                    Apertura match = matchOpt.get();
                    match.setHoraApertura(newApertura.getHoraApertura());
                    match.setHoraCierre(newApertura.getHoraCierre());
                    nuevasAperturas.add(match);
                } else {
                    newApertura.setEmpresa(existingEmpresa);
                    nuevasAperturas.add(newApertura);
                }
            }

            existingAperturas.clear();
            existingAperturas.addAll(nuevasAperturas);
        } else {
            existingEmpresa.getAperturas().clear();
        }
        return empresaRepository.save(existingEmpresa);
    }

    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }
}
