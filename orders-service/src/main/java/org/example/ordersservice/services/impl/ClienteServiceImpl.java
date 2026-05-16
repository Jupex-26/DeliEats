package org.example.ordersservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.exception.custom.RepartidorExistsException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.repositories.ClienteRepository;
import org.example.ordersservice.repositories.UserRepository;
import org.example.ordersservice.services.ClienteService;
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
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolService rolService;
    private final RepartidorService repartidorService;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    @Override
    public Cliente save(Cliente cliente) {
        if (userRepository.existsByEmail(cliente.getEmail())) {
            throw new EmailExistsException("El email " + cliente.getEmail() + " ya está registrado.");
        }

        String rawPassword = cliente.getPassword();

        if (Objects.isNull(rawPassword) || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException("La contraseña no cumple con el formato de seguridad requerido");
        }

        cliente.setPassword(passwordEncoder.encode(rawPassword));

        cliente.setRol(rolService.findByNombre("ROLE_CLIENTE"));

        return clienteRepository.save(cliente);
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + id));
    }

    @Override
    public Cliente update(Long id, Cliente cliente) {
        Cliente existente = findById(id);

        if (!existente.getEmail().equalsIgnoreCase(cliente.getEmail()) && userRepository.existsByEmail(cliente.getEmail())) {
            throw new EmailExistsException("El email " + cliente.getEmail() + " ya está en uso por otro usuario.");
        }

        cliente.setId(existente.getId());

        String rawPassword = cliente.getPassword();

        if (Objects.nonNull(rawPassword) && !rawPassword.isEmpty()) {

            if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
                throw new IllegalArgumentException("La nueva contraseña no cumple con el formato de seguridad");
            }

            cliente.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            cliente.setPassword(existente.getPassword());
        }

        cliente.setRol(rolService.findByNombre("ROLE_CLIENTE"));
        cliente.setFoto(existente.getFoto());

        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void solicitarSerRepartidor(Long id) {
        if (repartidorService.existsByClienteId(id)) {
            throw new RepartidorExistsException("Ya existe una solicitud o perfil de repartidor.");
        }

        Cliente cliente = findById(id);

        repartidorService.createFromCliente(cliente);
    }
}
