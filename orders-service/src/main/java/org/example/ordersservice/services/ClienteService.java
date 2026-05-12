package org.example.ordersservice.services;

import org.example.ordersservice.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    Cliente save(Cliente cliente);

    Page<Cliente> findAll(Pageable pageable);

    Cliente findById(Long id);

    Cliente update(Long id, Cliente cliente);

    void deleteById(Long id);

    void solicitarSerRepartidor(Long id);
}