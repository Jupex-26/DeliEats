package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.repositories.ClienteRepository;
import org.example.ordersservice.services.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Cliente save(Cliente cliente) {
        if (Objects.nonNull(cliente.getPassword())) {
            cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        }
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

        cliente.setId(existente.getId());
        
        if (Objects.nonNull(cliente.getPassword()) && !cliente.getPassword().isEmpty()) {
            cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        } else {
            cliente.setPassword(existente.getPassword());
        }

        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}
