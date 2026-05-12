package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.repositories.RepartidorRepository;
import org.example.ordersservice.services.RepartidorService;
import org.example.ordersservice.services.RolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repartidorRepository;
    private final PasswordEncoder passwordEncoder;

    private final RolService rolService;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");


    @Override
    public Repartidor save(Repartidor repartidor) {
        String rawPassword = repartidor.getPassword();

        if (Objects.isNull(rawPassword) || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException("La contraseña no cumple con el formato de seguridad requerido");
        }

        repartidor.setPassword(passwordEncoder.encode(rawPassword));

        repartidor.setRol(rolService.findByNombre("ROLE_REPARTIDOR"));
        return repartidorRepository.save(repartidor);
    }

    @Override
    public Page<Repartidor> findAll(Pageable pageable) {
        return repartidorRepository.findAll(pageable);
    }

    @Override
    public Repartidor findById(Long id) {
        return repartidorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Repartidor no encontrado con ID: " + id));
    }

    @Override
    public Page<Repartidor> findByDisponible(boolean disponible, Pageable pageable) {
        return repartidorRepository.findByDisponible(disponible, pageable);
    }

    @Override
    public Page<Repartidor> findByAprobado(boolean aprobado, Pageable pageable) {
        return repartidorRepository.findByAprobado(aprobado, pageable);
    }

    @Override
    public Repartidor update(Long id, Repartidor repartidor) {
        Repartidor existingRepartidor = findById(id);
        repartidor.setId(existingRepartidor.getId());

        repartidor.setAprobado(existingRepartidor.getAprobado());

        String rawPassword = repartidor.getPassword();

        if (Objects.nonNull(rawPassword) && !rawPassword.isEmpty()) {

            if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
                throw new IllegalArgumentException("La nueva contraseña no cumple con el formato de seguridad");
            }

            repartidor.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            repartidor.setPassword(existingRepartidor.getPassword());
        }

        repartidor.setRol(rolService.findByNombre("ROLE_REPARTIDOR"));

        return repartidorRepository.save(repartidor);
    }

    @Override
    public void deleteById(Long id) {
        repartidorRepository.deleteById(id);
    }

    @Override
    public Repartidor updateDisponibilidad(Long id, boolean disponible) {
        Repartidor repartidor = findById(id);
        repartidor.setDisponible(disponible);
        return repartidorRepository.save(repartidor);
    }

    @Override
    public boolean existsById(Long id) {
        return repartidorRepository.existsById(id);
    }

    @Override
    public void createFromCliente(Cliente cliente) {
        Repartidor repartidor = Repartidor.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .email(cliente.getEmail())
                .password(cliente.getPassword())
                .foto(cliente.getFoto())
                .aprobado(false)
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .disponible(false)
                .rol(cliente.getRol())
                .build();

        repartidorRepository.save(repartidor);
    }

    @Override
    public Repartidor aprobarRepartidor(Long id, boolean aprobado) {
        Repartidor repartidor = findById(id);
        repartidor.setAprobado(aprobado);
        return repartidorRepository.save(repartidor);
    }
}
