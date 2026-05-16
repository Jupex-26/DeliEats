package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.repositories.ClienteRepository;
import org.example.ordersservice.repositories.RepartidorRepository;
import org.example.ordersservice.repositories.UserRepository;
import org.example.ordersservice.services.RepartidorService;
import org.example.ordersservice.services.RolService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repartidorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    private final ClienteRepository clienteRepository;
    private final RolService rolService;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");


    @Override
    public Repartidor save(Repartidor repartidor) {
        if (userRepository.existsByEmail(repartidor.getEmail())) {
            throw new EmailExistsException("El email " + repartidor.getEmail() + " ya está registrado.");
        }

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

        if (!existingRepartidor.getEmail().equalsIgnoreCase(repartidor.getEmail()) && userRepository.existsByEmail(repartidor.getEmail())) {
            throw new EmailExistsException("El email " + repartidor.getEmail() + " ya está en uso por otro usuario.");
        }

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
    @Transactional
    public Repartidor updateDisponibilidad(Long id, boolean disponible) {
        Repartidor repartidor = findById(id);
        repartidor.setDisponible(disponible);

        if (disponible) {
            repartidor.setRol(rolService.findByNombre("ROLE_REPARTIDOR"));
            clienteRepository.deleteById(id);
        } else {
            repartidor.setRol(rolService.findByNombre("ROLE_CLIENTE"));
            if (!clienteRepository.existsById(id)) {
                Cliente cliente = new Cliente();
                cliente.setId(id);
                clienteRepository.save(cliente);
            }
        }

        Repartidor result = repartidorRepository.save(repartidor);
        entityManager.clear(); // limpiar caché de Hibernate
        return result;
    }


    @Override
    public void createFromCliente(Cliente cliente) {
        String sql = "INSERT INTO repartidor (id, disponible, aprobado) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, cliente.getId(), false, false);
    }

    @Override
    public void aprobarRepartidor(Long id, boolean aprobado) {
        Repartidor repartidor = findById(id);
        repartidor.setAprobado(aprobado);
        repartidorRepository.save(repartidor);
    }

    @Override
    public boolean existsById(Long id) {
        return repartidorRepository.existsById(id);
    }

    @Override
    public boolean isRepartidor(Long id) {
        String sql = "SELECT aprobado FROM repartidor WHERE id = ?";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

}
